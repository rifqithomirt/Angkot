package com.oesmanalie.it.angkot.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Points {
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("lyn")
    @Expose
    private String lyn;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lng;
    }

    public void setLon(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return nama;
    }

    public void setName(String nama) {
        this.nama = nama;
    }

    public String getLyn() {
        return lyn;
    }

    public void setLyn(String lyn) {
        this.lyn = lyn;
    }

    public LatLng getLatLng( ) {
        return new LatLng( this.lat, this.lng );
    }
}
