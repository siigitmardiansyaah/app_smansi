package com.tels.smansi.api;


import com.tels.smansi.model.ResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {

    @FormUrlEncoded
    @PUT("Mahasiswa")
    Call<ResponseData> profileResponse(
            @Field("id_siswa") String id_siswa,
            @Field("nama") String nama,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("Matkul")
    Call<ResponseData> matkulResponse(
            @Field("id_siswa") String id_siswa
    );

    @FormUrlEncoded
    @POST("Absen")
    Call<ResponseData> absenResponse(
            @Field("id_siswa") String id_siswa,
            @Field("id_mapel") String id_mapel
            );

    @FormUrlEncoded
    @POST("Absen/add")
    Call<ResponseData> absenData(
            @Field("id_jadwal") String id_jadwal,
            @Field("id_qr") String id_qr,
            @Field("id_siswa") String id_siswa,
            @Field("long_gps") double long_gps,
            @Field("lang_gps") double lang_gps
            );

    @FormUrlEncoded
    @POST("Login")
    Call<ResponseData> loginResponse(
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("device_id") String device_id
    );

    @FormUrlEncoded
    @POST("Register")
    Call<ResponseData> regisResponse(
            @Field("nis") String nis,
            @Field("password") String password,
            @Field("device_id") String device_id
    );


}
