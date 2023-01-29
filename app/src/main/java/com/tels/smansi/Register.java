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
import com.tels.smansi.model.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    TextInputEditText editTextNIS,editTextPassword,editTextPassword1;
    String nis,password,device_id,konfirmasi;
    Button btn_register;
    TextView login;
    ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextNIS = findViewById(R.id.editTextNisRegis);
        editTextPassword = findViewById(R.id.editTextPasswordRegis);
        editTextPassword1 = findViewById(R.id.editTextPasswordRegis1);
        btn_register = findViewById(R.id.cirRegisterButton);
        login = findViewById(R.id.txt_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nis = editTextNIS.getText().toString();
                password = editTextPassword.getText().toString();
                konfirmasi= editTextPassword1.getText().toString();
                device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<ResponseData> loginCall = apiInterface.regisResponse(nis,password,device_id);
                if(nis.length() < 1 && password.length() < 1) {
                    Toast.makeText(Register.this, "Kedua Kolom Harus di Isi", Toast.LENGTH_LONG).show();
                }else if(nis.length() < 1 || password.length() < 1) {
                    Toast.makeText(Register.this, "Kedua Kolom Harus di Isi", Toast.LENGTH_LONG).show();
                }else if(!password.equals(konfirmasi)){
                    Toast.makeText(Register.this, "Password Tidak Sama", Toast.LENGTH_LONG).show();
                }else{
                    loginCall.enqueue(new Callback<ResponseData>() {
                        @Override
                        public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                            if(response.isSuccessful() && !response.body().isError()){
                                //Ini untuk pindah
                                Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseData> call, Throwable t) {
//                        Toast.makeText(Register.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}