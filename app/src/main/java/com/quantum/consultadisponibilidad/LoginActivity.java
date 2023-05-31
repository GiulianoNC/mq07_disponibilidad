package com.quantum.consultadisponibilidad;

import static com.quantum.consultadisponibilidad.Configuracion.direc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.consultadisponibilidad.conectividad.Conexion;
import com.quantum.consultadisponibilidad.parseoMQ0701A.CuerpoA;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextView user, password, resultadoConexion, url;
    public static String usuarioGlobal = null;
    public static String claveGlobal = null;
    private TextView qtm;

    Switch switcher;
    boolean nightMODE;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.Usuario);
        password = findViewById(R.id.contras);
        resultadoConexion= findViewById(R.id.informeConexion);

        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        user.setText(preferences.getString("usuario",""));
        password.setText(preferences.getString("password",""));
        //guardar();
        url = findViewById(R.id.dir);
        String direccion = getIntent().getStringExtra("direcciones");
        url.setText(direccion);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        qtm = findViewById(R.id.QTMtitulo);
        qtm.setText("QTM - DISPONIBILIDAD" );

        //Esto es el Day/Night Mode
        //Uso el SharedPreference para guardar el modo cuando salgo de la pagina
        switcher = findViewById(R.id.btnToggleDark);
        sharedPreferences = getSharedPreferences("MODE",Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night",false); //El modo luz es el default

        if (nightMODE){
            switcher.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",true);
                }
                editor.apply();
            }
        });
    }

    //metodo para guardar
    public void guardar (){

        SharedPreferences preferecias =  getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();
        Obj_editor.putString("usuario", user.getText().toString());
        Obj_editor.putString("password", password.getText().toString());
        Obj_editor.commit();
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    //acciones de menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_privacidad:
                Toast.makeText(this, "Politicas de privacidad", Toast.LENGTH_SHORT).show();

                String url = "https://quantumconsulting.com.ar/politicas-de-privacidad/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.action_configuracion:
                Intent siguiente = new Intent(LoginActivity.this, Configuracion.class);
                startActivity(siguiente);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void ingresar(View view ) {

        String user2 = user.getText().toString();
        String clave2 = password.getText().toString();
        String direccion = getIntent().getStringExtra("direcciones");
        url.setText(direccion);

        if (user2.length() == 0 && clave2.length() == 0) {
            Toast.makeText(this, "Debes ingresar un usuario y contraseña", Toast.LENGTH_LONG).show();
        }

       if (user2.length() != 0 && clave2.length() != 0) {
           if (url.length() == 0) {

               Intent siguiente = new Intent(LoginActivity.this, Configuracion.class);
               startActivity(siguiente);
           } else {
               Toast.makeText(LoginActivity.this, "Procesando", Toast.LENGTH_LONG).show();

               usuarioGlobal = user.getText().toString();
               claveGlobal = password.getText().toString();

               Retrofit retrofit = new Retrofit.Builder()
                       .baseUrl(direc)
                       .addConverterFactory(GsonConverterFactory.create())
                       .build();

               Conexion conexion = retrofit.create(Conexion.class);

               CuerpoA loguerse = new CuerpoA(user2, clave2);

               Call<CuerpoA> call = conexion.getDataA(loguerse);
               call.enqueue(new Callback<CuerpoA>() {
                   @Override
                   public void onResponse(Call<CuerpoA> call, Response<CuerpoA> response) {
                       int statusCode = response.code();
                       if (response.isSuccessful()) {

                           CuerpoA cuerpo = response.body();

                           if (statusCode == 200) {
                               Intent siguiente = new Intent(LoginActivity.this, PantallaPrincipal.class);
                               startActivity(siguiente);
                           }

                       }
                       else {
                           if (statusCode == 403) {
                               Toast.makeText(LoginActivity.this, "Usuario/Contraseña Incorrecto", Toast.LENGTH_LONG).show();
                           }
                           if (statusCode == 500) {
                               Toast.makeText(LoginActivity.this, "Error en el servidor", Toast.LENGTH_LONG).show();

                           }
                       }
                   }

                   @Override
                   public void onFailure(Call<CuerpoA> call, Throwable t) {
                       Toast.makeText(LoginActivity.this, "Falló el login", Toast.LENGTH_LONG).show();
                       resultadoConexion.setText(t.getMessage());
                   }
               });
           }
       }

        SharedPreferences preferecias =  getSharedPreferences("datos",Context.MODE_PRIVATE);
        SharedPreferences.Editor Obj_editor = preferecias.edit();
        Obj_editor.putString("usuario", user.getText().toString());
        Obj_editor.putString("password", password.getText().toString());

        Obj_editor.commit();

    }
}
