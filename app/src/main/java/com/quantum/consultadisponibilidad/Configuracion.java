package com.quantum.consultadisponibilidad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.quantum.consultadisponibilidad.R;

public class Configuracion extends AppCompatActivity {
    private TextView direccion,qtm;
    public static String direc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        direccion= findViewById(R.id.direccion);

        SharedPreferences preferences = getSharedPreferences("dato", Context.MODE_PRIVATE);
        direccion.setText(preferences.getString("direcciones",""));

        direc = direccion.getText().toString();
        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color
        qtm = findViewById(R.id.QTM);
        qtm.setText("QTM - DISPONIBILIDAD   " );
    }


    public void guardar (View view){
        SharedPreferences preferecias =  getSharedPreferences("dato",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();

        Obj_editor.putString("direcciones", direccion.getText().toString());
        Obj_editor.commit();
        Intent siguiente = new Intent(Configuracion.this, LoginActivity.class);
        siguiente.putExtra("direcciones", direccion.getText().toString());
        startActivity(siguiente);
    }

}
