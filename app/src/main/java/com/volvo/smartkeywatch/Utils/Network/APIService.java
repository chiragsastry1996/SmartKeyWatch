package com.volvo.smartkeywatch.Utils.Network;

import com.google.gson.JsonObject;
import com.volvo.smartkeywatch.Utils.Models.Door;
import com.volvo.smartkeywatch.Utils.Models.DoorPost;
import com.volvo.smartkeywatch.Utils.Models.HVAC;
import com.volvo.smartkeywatch.Utils.Models.HVACPost;
import com.volvo.smartkeywatch.Utils.Models.Headlamp;
import com.volvo.smartkeywatch.Utils.Models.HeadlampPost;

import org.json.JSONObject;
import org.json.JSONStringer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("doorstatus")
    Call<Door> getDoorStatus();

    @POST("doorcontrol")
    Call<DoorPost> postDoorStatus (@Body JsonObject doorPost);

    @GET("headlampstatus")
    Call<Headlamp> getHeadlampStatus();

    @POST("headlampcontrol")
    Call<HeadlampPost> postHeadlampStatus (@Body JsonObject headlampPost);

    @GET("acstatus")
    Call<HVAC> getHVACStatus();

    @POST("accontrol")
    Call<HVACPost> postHVACStatus (@Body JsonObject HVACPost);

}