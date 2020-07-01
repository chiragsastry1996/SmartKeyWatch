package com.volvo.smartkeywatch.Utils.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoorPost {

    @SerializedName("doorstatus")
    @Expose
    private String doorstatus;

    public String getDoorstatus() {
        return doorstatus;
    }

    public void setDoorstatus(String doorstatus) {
        this.doorstatus = doorstatus;
    }

}