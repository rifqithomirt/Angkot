package com.oesmanalie.it.angkot.models;

public class AngkotModel {
    private String namaAngkot;
    private String imageUrl;

    public AngkotModel(String namaAngkot, String imageUrl) {
        this.namaAngkot = namaAngkot;
        this.imageUrl = imageUrl;
    }

    public String getNamaAngkot() {
        return namaAngkot;
    }

    public void setNamaAngkot(String namaAngkot) {
        this.namaAngkot = namaAngkot;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
