package com.oesmanalie.it.angkot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("desc")
    @Expose
    private String desc;

    public String getMsg() {
        return msg;
    }

    public String getDesc() {
        return desc;
    }

}
