package com.volvo.smartkeywatch.Utils.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeadlampPost {

    @SerializedName("headlampstatus")
    @Expose
    private String headlampstatus;

    public String getHeadlampstatus() {
        return headlampstatus;
    }

    public void setHeadlampstatus(String headlampstatus) {
        this.headlampstatus = headlampstatus;
    }

}