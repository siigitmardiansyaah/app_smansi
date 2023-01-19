package com.tels.smansi.model;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("id_siswa")
    private String id_siswa;

    @SerializedName("nama")
    private String nama;

    @SerializedName("id_kelas")
    private String id_kelas;

    @SerializedName("device_id")
    private String device_id;

    @SerializedName("nis")
    private String nis;

    @SerializedName("nama_kelas")
    private String nama_kelas;


    public String getId_siswa() {
        return id_siswa;
    }

    public void setId_siswa(String id_siswa) {
        this.id_siswa = id_siswa;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(String id_kelas) {
        this.id_kelas = id_kelas;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String nama_kelas) {
        this.nama_kelas = nama_kelas;
    }
}
