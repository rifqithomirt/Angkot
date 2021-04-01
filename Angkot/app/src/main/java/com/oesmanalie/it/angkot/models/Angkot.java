package com.oesmanalie.it.angkot.models;

public class Angkot {
    private String angkotName;
    private String angkotColor;

    public Angkot(String angkotName, String angkotColor) {
        this.angkotName = angkotName;
        this.angkotColor = angkotColor;
    }

    public String getAngkotName() {
        return angkotName;
    }

    public void setAngkotName(String angkotName) {
        this.angkotName = angkotName;
    }

    public String getAngkotColor() {
        return angkotColor;
    }

    public void setAngkotColor(String angkotColor) {
        this.angkotColor = angkotColor;
    }
}
