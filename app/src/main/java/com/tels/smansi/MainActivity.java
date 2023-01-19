package com.tels.smansi;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tels.smansi.api.ApiClient;
import com.tels.smansi.api.ApiInterface;
import com.tels.smansi.model.ResponseData;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    double long_gps;
    double lang_gps;
    String id_siswa, id_jadwal, hasil, id_qr, id_mapel, hiya1, hiya2;
    String[] kata;
    GpsTracker gpsTracker;
    ApiInterface apiInterface;
    private Context context;
    SessionManager sessionManager;
    TextView txt_namauser, txt_kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(MainActivity.this);
        if (!sessionManager.isLoggedIn()) {
            moveToLogin();
        }
        CardView cardViewScan = findViewById(R.id.scanqr);
        CardView cardViewMapel = findViewById(R.id.mapel);
        CardView cardViewProfile = findViewById(R.id.profile);
        CardView cardViewLogout = findViewById(R.id.logout);
        txt_namauser = findViewById(R.id.txt_namauser);
        txt_kelas = findViewById(R.id.hiyaaaa);
        context = this;

        txt_namauser.setText(sessionManager.getUserDetail().get(SessionManager.NAMA));
        txt_kelas.setText("Kelas : " + sessionManager.getUserDetail().get(SessionManager.NAMA_KELAS));


        cardViewScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan QRcode");
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cardViewMapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Mapel.class));
            }
        });

        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda Yakin Ingin Logout ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sessionManager.logoutSession();
                        moveToLogin();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        IntentResult result =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Keluar",
                        Toast.LENGTH_LONG).show();
            } else {
                id_siswa = sessionManager.getUserDetail().get(SessionManager.ID_SISWA);
                gpsTracker = new GpsTracker(MainActivity.this);
                if (gpsTracker.canGetLocation()) {
                    lang_gps = gpsTracker.getLatitude();
                    long_gps = gpsTracker.getLongitude();
                } else {
                    gpsTracker.showSettingsAlert();
                }
                hasil = result.getContents();
                String[] kata = hasil.split("-");
                id_jadwal = kata[0];
                id_qr = kata[1];
                id_mapel = kata[5];

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("Apakah Anda Yakin Ingin Absen ?").setContentText("Pastikan Mata Pelajarannya sudah sesuai").setConfirmText("Iya").showCancelButton(false).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<ResponseData> sendbio = apiInterface.absenData(id_jadwal, id_qr, id_siswa, long_gps, lang_gps);
                        sendbio.enqueue(new Callback<ResponseData>() {
                            @Override
                            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                if (response.body().isError() == false) {
                                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, Absensi.class);
                                    intent.putExtra("id_mapel", id_mapel);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseData> call, Throwable t) {
                                Log.d("RETRO", "Falure : " + "Gagal Mengirim Request");
                            }
                        });
                    }
                }).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    private void moveToLogin() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        sessionManager.logoutSession();
        startActivity(intent);
        finish();
    }

    public void checkPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}