package com.oesmanalie.it.angkot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PenumpangPosition {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("rute_id")
    @Expose
    private String rute_id;
    @SerializedName("startPoint")
    @Expose
    private String startPoint;

    public String getID() {
        return id;
    }
    public String getRuteID() {
        return rute_id;
    }
    public String getStartPoint() {
        return startPoint;
    }

}
