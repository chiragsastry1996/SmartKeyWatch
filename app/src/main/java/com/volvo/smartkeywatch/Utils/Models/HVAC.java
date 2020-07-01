package com.volvo.smartkeywatch.Utils.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HVAC {

    @SerializedName("acstatus")
    @Expose
    private String acStatus;

    public String getAcStatus() {
        return acStatus;
    }

    public void setAcStatus(String acStatus) {
        this.acStatus = acStatus;
    }

}