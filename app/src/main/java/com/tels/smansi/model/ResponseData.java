package com.tels.smansi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData {
    private List<MapelData> matkul;
    private List<AbsenData> absen;
    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private boolean error;
    @SerializedName("login")
    private LoginData login;

    public List<MapelData> getMatkul() {
        return matkul;
    }

    public void setMatkul(List<MapelData> matkul) {
        this.matkul = matkul;
    }

    public List<AbsenData> getAbsen() {
        return absen;
    }

    public void setAbsen(List<AbsenData> absen) {
        this.absen = absen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public LoginData getLogin() {
        return login;
    }

    public void setLogin(LoginData login) {
        this.login = login;
    }
}