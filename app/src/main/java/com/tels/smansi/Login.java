package com.tels.smansi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.tels.smansi.api.ApiClient;
import com.tels.smansi.api.ApiInterface;
import com.tels.smansi.model.LoginData;
import com.tels.smansi.model.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    TextInputEditText editTextNIS,editTextPassword;
    String nis,password,device_id;
    Button btn_login;
    TextView register;
    ApiInterface apiInterface;
    SessionManager sessionManager;
    private long pressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNIS = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btn_login = findViewById(R.id.cirLoginButton);
        register = findViewById(R.id.txt_regis);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nis = editTextNIS.getText().toString();
                password = editTextPassword.getText().toString();
                device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseData> loginCall = apiInterface.loginResponse(nis,password,device_id);
                if(nis.length() < 1 && password.length() < 1) {
                    Toast.makeText(Login.this, "Kedua Kolom Harus di Isi", Toast.LENGTH_LONG).show();
                }else if(nis.length() < 1 || password.length() < 1) {
                    Toast.makeText(Login.this, "Kedua Kolom Harus di Isi", Toast.LENGTH_LONG).show();
                }else{
                    loginCall.enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if(response.isSuccessful() && response.body().isError() == false){

                                // Ini untuk menyimpan sesi
                                sessionManager = new SessionManager(Login.this);
                                LoginData loginData = response.body().getLogin();
                                sessionManager.createLoginSession(loginData);

                                //Ini untuk pindah
                                Toast.makeText(Login.this,"Selamat Datang " + response.body().getLogin().getNama(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
//                        Toast.makeText(Login.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Ketuk lagi untuk keluar", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();

    }
}