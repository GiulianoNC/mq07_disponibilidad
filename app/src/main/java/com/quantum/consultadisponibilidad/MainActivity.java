package com.quantum.consultadisponibilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.quantum.consultadisponibilidad.R;

public class MainActivity extends AppCompatActivity {
    VideoView videoview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color


        //video
        videoview  = (VideoView)findViewById(R.id.videoPresentacion);
        String uriPath = "android.resource://com.quantum.consultadisponibilidad/"+R.raw.inicio_isotipo_disponibilidad;
        Uri uri = Uri.parse(uriPath);
        videoview.setVideoURI(uri);
        videoview.requestFocus();
        videoview.start();

        //full screem la q con
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //splash Screem
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_ConsultaDisponibilidad);
        //tiempo de la pantalla Q

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },7000);
    }
}