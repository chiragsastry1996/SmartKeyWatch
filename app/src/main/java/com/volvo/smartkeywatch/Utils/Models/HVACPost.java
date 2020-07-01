package com.volvo.smartkeywatch.Utils.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HVACPost {

    @SerializedName("acstatus")
    @Expose
    private String acstatus;

    public String getAcstatus() {
        return acstatus;
    }

    public void setAcstatus(String acstatus) {
        this.acstatus = acstatus;
    }

}