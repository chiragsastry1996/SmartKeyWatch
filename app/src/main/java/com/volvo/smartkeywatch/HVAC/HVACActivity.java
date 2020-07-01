package com.volvo.smartkeywatch.HVAC;

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
import com.volvo.smartkeywatch.Utils.Models.HVAC;
import com.volvo.smartkeywatch.Utils.Models.HVACPost;
import com.volvo.smartkeywatch.Utils.Network.APIService;
import com.volvo.smartkeywatch.Utils.Network.NetworkConnectionInterceptor;
import com.volvo.smartkeywatch.Utils.Network.RetrofitClient;

import java.io.IOException;

import static android.content.ContentValues.TAG;

public class HVACActivity extends WearableActivity {
    @BindView(R.id.hvac_activity)
    RelativeLayout relativeLayout;
    @BindView(R.id.hvac_status)
    TextView hvac_status;
    @BindView(R.id.hvac_image)
    ImageView hvac_image;
    @BindView(R.id.hvac_button)
    Button hvac_button;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hvac);
        ButterKnife.bind(this);

        request_hvac_status();

        hvac_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_hvac_status(status);
            }
        });

    }

    public void request_hvac_status(){
        APIService service = RetrofitClient.getClient(this, relativeLayout).create(APIService.class);
        service.getHVACStatus().enqueue(new Callback<HVAC>() {
            @Override
            public void onResponse(Call<HVAC> call, Response<HVAC> response) {
                if(response.isSuccessful()) {
                    if(response.body()!=null){
                        HVAC hvac = response.body();

                        if(hvac.getAcStatus().equals("ON")){
                            hvac_button.setText("OFF");
                            hvac_image.setImageDrawable(getResources().getDrawable(R.drawable.hvac, getApplicationContext().getTheme()));
                            hvac_status.setText("Status: AC is On");
                            status = "ON";
                        }
                        else if(hvac.getAcStatus().equals("OFF")){
                            hvac_button.setText("ON");
                            hvac_image.setImageDrawable(getResources().getDrawable(R.drawable.hvac, getApplicationContext().getTheme()));
                            hvac_status.setText("Status: AC is Off");
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
            public void onFailure(Call<HVAC> call, Throwable t) {
                if(!(t instanceof NetworkConnectionInterceptor.NoConnectivityException))
                    GlobalClass.snackbar(relativeLayout,"Oops, something went wrong");
                Log.e(TAG, "Unable to submit post to API. " + t.getMessage());
            }
        });
    }

    public void post_hvac_status(String current_status){
        JsonObject statusObject = new JsonObject();
        String command = "";
        if(current_status.isEmpty()){
            GlobalClass.snackbar(relativeLayout,"Current status unavailable. Can't send command when unknown");
        } else if(current_status.equals("ON")){
            command = "OFF";
            statusObject.addProperty("acstatus",command);
        }else if(current_status.equals("OFF")){
            command = "ON";
            statusObject.addProperty("acstatus",command);
        }else GlobalClass.snackbar(relativeLayout, "Invalid status. Can't execute command");

        APIService service = RetrofitClient.getClient(this, relativeLayout).create(APIService.class);
        service.postHVACStatus(statusObject).enqueue(new Callback<HVACPost>() {
            @Override
            public void onResponse(Call<HVACPost> call, Response<HVACPost> response) {
                if(response.isSuccessful()) {
                    if(response.body()!=null){

                        request_hvac_status();

                        String post_status = response.body().getAcstatus();

                        if(post_status.equals("ON")){
                            hvac_button.setText("OFF");
                            hvac_status.setText("Status: AC is On");
                            status = "ON";
                        }
                        else if(post_status.equals("OFF")){
                            hvac_button.setText("ON");
                            hvac_status.setText("Status: AC is Off");
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
            public void onFailure(Call<HVACPost> call, Throwable t) {
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
