package com.volvo.smartkeywatch.Door;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.volvo.smartkeywatch.MainMenu.MainMenuActivity;
import com.volvo.smartkeywatch.R;
import com.volvo.smartkeywatch.Utils.GlobalClass;
import com.volvo.smartkeywatch.Utils.Models.Door;
import com.volvo.smartkeywatch.Utils.Models.DoorPost;
import com.volvo.smartkeywatch.Utils.Network.APIService;
import com.volvo.smartkeywatch.Utils.Network.NetworkConnectionInterceptor;
import com.volvo.smartkeywatch.Utils.Network.RetrofitClient;
import com.volvo.smartkeywatch.R;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class DoorActivity extends WearableActivity {
    @BindView(R.id.door_activity)
    RelativeLayout relativeLayout;
    @BindView(R.id.door_status)
    TextView door_status;
    @BindView(R.id.door_image)
    ImageView door_image;
    @BindView(R.id.door_button)
    Button door_button;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);
        ButterKnife.bind(this);

        request_door_status();

        door_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_door_status(status);
            }
        });

    }

    public void request_door_status(){
        APIService service = RetrofitClient.getClient(this, relativeLayout).create(APIService.class);
        service.getDoorStatus().enqueue(new Callback<Door>() {
            @Override
            public void onResponse(Call<Door> call, Response<Door> response) {
                if(response.isSuccessful()) {
                    if(response.body()!=null){
                        Door door = response.body();

                        if(door.getDoorstatus().equals("UNLOCKED")){
                           door_button.setText("LOCK");
                           door_image.setImageDrawable(getResources().getDrawable(R.drawable.door_unlock, getApplicationContext().getTheme()));
                           door_status.setText("Status: Unlocked");
                           status = "UNLOCKED";
                        }
                        else if(door.getDoorstatus().equals("LOCKED")){
                            door_button.setText("UNLOCK");
                            door_image.setImageDrawable(getResources().getDrawable(R.drawable.door_lock, getApplicationContext().getTheme()));
                            door_status.setText("Status: Locked");
                            status = "LOCKED";
                        }

                    }
                    else GlobalClass.snackbar(relativeLayout, "Error parsing details");

                }
                 else{
                    GlobalClass.snackbar(relativeLayout,response.message());
                }
            }

            @Override
            public void onFailure(Call<Door> call, Throwable t) {
                if(!(t instanceof NetworkConnectionInterceptor.NoConnectivityException))
                    GlobalClass.snackbar(relativeLayout,"Oops, something went wrong");
                Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
            }
        });
    }

    public void post_door_status(String current_status){
        JsonObject statusObject = new JsonObject();
        String command = "";
        if(current_status.isEmpty()){
            GlobalClass.snackbar(relativeLayout,"Current status unavailable. Can't send command when unknown");
        } else if(current_status.equals("UNLOCKED")){
            command = "LOCKED";
            statusObject.addProperty("doorstatus",command);
        }else if(current_status.equals("LOCKED")){
            command = "UNLOCKED";
            statusObject.addProperty("doorstatus",command);
        }else GlobalClass.snackbar(relativeLayout, "Invalid status. Can't execute command");

        APIService service = RetrofitClient.getClient(this, relativeLayout).create(APIService.class);
        service.postDoorStatus(statusObject).enqueue(new Callback<DoorPost>() {
            @Override
            public void onResponse(Call<DoorPost> call, Response<DoorPost> response) {
                if(response.isSuccessful()) {
                    if(response.body()!=null){

                        request_door_status();
                        String post_status = response.body().getDoorstatus();


                        if(post_status.equals("UNLOCKED")){
                            door_button.setText("LOCK");
                            door_status.setText("Status: Unlocked");
                            status = "UNLOCKED";
                        }
                        else if(post_status.equals("LOCKED")){
                            door_button.setText("UNLOCK");
                            door_status.setText("Status: Locked");
                            status = "LOCKED";
                        }

                    }
                    else GlobalClass.snackbar(relativeLayout, "Error parsing details");

                }
                else{
                    GlobalClass.snackbar(relativeLayout,response.message());
                }
            }

            @Override
            public void onFailure(Call<DoorPost> call, Throwable t) {
                if(!(t instanceof NetworkConnectionInterceptor.NoConnectivityException))
                    GlobalClass.snackbar(relativeLayout,"Oops, something went wrong");
                Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent back = new Intent(this, MainMenuActivity.class);
        startActivity(back);
        finish();
    }

}
