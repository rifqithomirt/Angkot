package com.oesmanalie.it.angkot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Routes {

    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;
    @SerializedName("nama_angkot")
    @Expose
    private String namaAngkot;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNamaAngkot() {
        return namaAngkot;
    }

    public void setNamaAngkot(String namaAngkot) {
        this.namaAngkot = namaAngkot;
    }
}