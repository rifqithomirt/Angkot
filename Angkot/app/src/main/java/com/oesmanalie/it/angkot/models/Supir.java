package com.oesmanalie.it.angkot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supir {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("jenis_kelamin")
    @Expose
    private String jenisKelamin;
    @SerializedName("tanggal_lahir")
    @Expose
    private String tanggalLahir;
    @SerializedName("tinggi_badan")
    @Expose
    private Integer tinggiBadan;
    @SerializedName("ktp")
    @Expose
    private String ktp;
    @SerializedName("sim_a")
    @Expose
    private String simA;
    @SerializedName("sim_b1")
    @Expose
    private String simB1;
    @SerializedName("aktif")
    @Expose
    private Boolean aktif;
    @SerializedName("nohp")
    @Expose
    private String nohp;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password_supir")
    @Expose
    private String passwordSupir;
    @SerializedName("rute_id")
    @Expose
    private Integer ruteId;
    @SerializedName("nama_rute")
    @Expose
    private String namaRute;
    @SerializedName("asal")
    @Expose
    private String asal;
    @SerializedName("tujuan")
    @Expose
    private String tujuan;
    @SerializedName("jumlahangkot")
    @Expose
    private Integer jumlahangkot;
    @SerializedName("warna_id")
    @Expose
    private Integer warnaId;
    @SerializedName("nama_angkot")
    @Expose
    private String namaAngkot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public Integer getTinggiBadan() {
        return tinggiBadan;
    }

    public void setTinggiBadan(Integer tinggiBadan) {
        this.tinggiBadan = tinggiBadan;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getSimA() {
        return simA;
    }

    public void setSimA(String simA) {
        this.simA = simA;
    }

    public String getSimB1() {
        return simB1;
    }

    public void setSimB1(String simB1) {
        this.simB1 = simB1;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordSupir() {
        return passwordSupir;
    }

    public void setPasswordSupir(String passwordSupir) {
        this.passwordSupir = passwordSupir;
    }

    public Integer getRuteId() {
        return ruteId;
    }

    public void setRuteId(Integer ruteId) {
        this.ruteId = ruteId;
    }

    public String getNamaRute() {
        return namaRute;
    }

    public void setNamaRute(String namaRute) {
        this.namaRute = namaRute;
    }

    public String getAsal() {
        return asal;
    }

    public void setAsal(String asal) {
        this.asal = asal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public Integer getJumlahangkot() {
        return jumlahangkot;
    }

    public void setJumlahangkot(Integer jumlahangkot) {
        this.jumlahangkot = jumlahangkot;
    }

    public Integer getWarnaId() {
        return warnaId;
    }

    public void setWarnaId(Integer warnaId) {
        this.warnaId = warnaId;
    }

    public String getNamaAngkot() {
        return namaAngkot;
    }

    public void setNamaAngkot(String namaAngkot) {
        this.namaAngkot = namaAngkot;
    }

}