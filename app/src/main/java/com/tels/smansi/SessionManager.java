package com.tels.smansi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.tels.smansi.model.LoginData;

import java.util.HashMap;

public class SessionManager {
    private Context _context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String IS_LOGGED_IN = "isLoggedIn";

    //DATA SESSION LOGIN
    public static final String ID_SISWA = "id_siswa";
    public static final String NAMA = "nama";
    public static final String ID_KELAS = "id_kelas";
    public static final String DEVICE_ID = "device_id";
    public static final String NIS = "nis";
    public static final String NAMA_KELAS = "nama_kelas";



    public SessionManager(Context context){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(LoginData user){
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(ID_SISWA, user.getId_siswa());
        editor.putString(NAMA, user.getNama());
        editor.putString(ID_KELAS, user.getId_kelas());
        editor.putString(DEVICE_ID, user.getDevice_id());
        editor.putString(NIS, user.getNis());
        editor.putString(NAMA_KELAS, user.getNama_kelas());
        editor.commit();
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID_SISWA, sharedPreferences.getString(ID_SISWA,null));
        user.put(NAMA, sharedPreferences.getString(NAMA,null));
        user.put(ID_KELAS, sharedPreferences.getString(ID_KELAS,null));
        user.put(DEVICE_ID, sharedPreferences.getString(DEVICE_ID,null));
        user.put(NIS, sharedPreferences.getString(NIS,null));
        user.put(NAMA_KELAS, sharedPreferences.getString(NAMA_KELAS,null));
        return user;
    }

    public void logoutSession(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

}
