package com.volvo.smartkeywatch.Headlamp;

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
import com.volvo.smartkeywatch.Utils.Models.Headlamp;
import com.volvo.smartkeywatch.Utils.Models.HeadlampPost;
import com.volvo.smartkeywatch.Utils.Network.APIService;
import com.volvo.smartkeywatch.Utils.Network.NetworkConnectionInterceptor;
import com.volvo.smartkeywatch.Utils.Network.RetrofitClient;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class HeadlampActivity extends WearableActivity {
    @BindView(R.id.headlamp_activity)
    RelativeLayout relativeLayout;
    @BindView(R.id.headlamp_status)
    TextView headlamp_status;
    @BindView(R.id.headlamp_image)
    ImageView headlamp_image;
    @BindView(R.id.headlamp_button)
    Button headlamp_button;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlamp);
        ButterKnife.bind(this);

        request_headlamp_status();

        headlamp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_headlamp_status(status);
            }
        });

    }

    public void request_headlamp_status(){
        APIService service = RetrofitClient.getClient(this, relativeLayout).create(APIService.class);
        service.getHeadlampStatus().enqueue(new Callback<Headlamp>() {
            @Override
            public void onResponse(Call<Headlamp> call, Response<Headlamp> response) {
                if(response.isSuccessful()) {
                    if(response.body()!=null){
                        Headlamp headlamp = response.body();

                        if(headlamp.getHeadlampstatus().equals("ON")){
                            headlamp_button.setText("OFF");
                            headlamp_image.setImageDrawable(getResources().getDrawable(R.drawable.headlight, getApplicationContext().getTheme()));
                            headlamp_status.setText("Status: Headlight is On");
                            status = "ON";
                        }
                        else if(headlamp.getHeadlampstatus().equals("OFF")){
                            headlamp_button.setText("ON");
                            headlamp_image.setImageDrawable(getResources().getDrawable(R.drawable.headlight, getApplicationContext().getTheme()));
                            headlamp_status.setText("Status: Headlight is Off");
                            status = "OFF";
                        }

                    }
                    else GlobalClass.snackbar(relativeLayout, "Error parsing details");

                }
                else{
                    GlobalClass.snackbar(relativeLayout,response.message());
                }
            }

            @Override
            public void onFailure(Call<Headlamp> call, Throwable t) {
                if(!(t instanceof NetworkConnectionInterceptor.NoConnectivityException))
                    GlobalClass.snackbar(relativeLayout,"Oops, something went wrong");
                Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
            }
        });
    }

    public void post_headlamp_status(String current_status){
        JsonObject statusObject = new JsonObject();
        String command = "";
        if(current_status.isEmpty()){
            GlobalClass.snackbar(relativeLayout,"Current status unavailable. Can't send command when unknown");
        } else if(current_status.equals("ON")){
            command = "OFF";
            statusObject.addProperty("headlampstatus",command);
        }else if(current_status.equals("OFF")){
            command = "ON";
            statusObject.addProperty("headlampstatus",command);
        }else GlobalClass.snackbar(relativeLayout, "Invalid status. Can't execute command");

        APIService service = RetrofitClient.getClient(this, relativeLayout).create(APIService.class);
        service.postHeadlampStatus(statusObject).enqueue(new Callback<HeadlampPost>() {
            @Override
            public void onResponse(Call<HeadlampPost> call, Response<HeadlampPost> response) {
                if(response.isSuccessful()) {
                    if(response.body()!=null){

                        request_headlamp_status();

                        String post_status = response.body().getHeadlampstatus();

                        if(post_status.equals("ON")){
                            headlamp_button.setText("OFF");
                            headlamp_status.setText("Status: Headlight is On");
                            status = "ON";
                        }
                        else if(post_status.equals("OFF")){
                            headlamp_button.setText("ON");
                            headlamp_status.setText("Status: Headlight is Off");
                            status = "OFF";
                        }

                    }
                    else GlobalClass.snackbar(relativeLayout, "Error parsing details");

                }
                else{
                    GlobalClass.snackbar(relativeLayout,response.message());
                }
            }

            @Override
            public void onFailure(Call<HeadlampPost> call, Throwable t) {
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
