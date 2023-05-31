package com.quantum.consultadisponibilidad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.quantum.consultadisponibilidad.Configuracion.direc;
import static com.quantum.consultadisponibilidad.LoginActivity.claveGlobal;
import static com.quantum.consultadisponibilidad.LoginActivity.usuarioGlobal;
import static com.quantum.consultadisponibilidad.PantallaPrincipal.plantaGlobal;
import static com.quantum.consultadisponibilidad.PantallaPrincipal.codItemGlobal;
import static com.quantum.consultadisponibilidad.PantallaPrincipal.descItemGlobal;
import static com.quantum.consultadisponibilidad.PantallaPrincipal.UMGlobal;
import static com.quantum.consultadisponibilidad.PantallaPrincipal.existenciasGlobal;

import com.quantum.consultadisponibilidad.adaptador.SegundoAdapterDatos;
import com.quantum.consultadisponibilidad.conectividad.Conexion;
import com.quantum.consultadisponibilidad.parseoMQ0701D.CuerpoD;
import com.quantum.consultadisponibilidad.parseoMQ0701D.Mq0701dFormreq319;
import com.quantum.consultadisponibilidad.parseoMQ0701D.RowsetD;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SegundaPantalla extends AppCompatActivity {

    private TextView item, descItem, planta, qtm, um, total, ubicacion, lote, exis, compr, disp;
    ArrayList<String> listDatosD;
    private Integer statusCode;
    RecyclerView recyclerD;
    private ProgressBar progressBar;

    public static int borrarGlobal = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_pantalla);
        // splash Screem
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

        setTheme(R.style.Theme_ConsultaDisponibilidad);
        qtm = findViewById(R.id.QTMtitulo);
        qtm.setText("QTM - DISPONIBILIDAD" );
        item=findViewById(R.id.item);
        descItem=findViewById(R.id.descItem);
        planta=findViewById(R.id.planta);
        um=findViewById(R.id.UM);
        total=findViewById(R.id.total);
        recyclerD=(RecyclerView) findViewById(R.id.psD);
        recyclerD.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ubicacion=findViewById(R.id.ubicacion);
        lote=findViewById(R.id.lote);
        exis=findViewById(R.id.exist);
        compr=findViewById(R.id.compr);
        disp=findViewById(R.id.disp);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        traerDatos();
        buscarDatos();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color

    }

    private void traerDatos() {
        progressBar.setVisibility(View.VISIBLE);
        item.setText("  "+codItemGlobal+"  ");
        descItem.setText(" "+descItemGlobal+"  ");
        planta.setText("  "+plantaGlobal+"  ");
        um.setText("  "+UMGlobal+"  ");
        total.setText("    TOTAL "+existenciasGlobal+"     ");

    }
    private void buscarDatos(){

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(direc)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Conexion conexion = retrofit.create(Conexion.class);
        CuerpoD cuerpoD = new CuerpoD(usuarioGlobal,claveGlobal,plantaGlobal,codItemGlobal);

        Call<CuerpoD> call = conexion.getDataD(cuerpoD);
        call.enqueue(new Callback<CuerpoD>() {
            @Override
            public void onResponse(Call<CuerpoD> call, Response<CuerpoD> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Mq0701dFormreq319 datosProductoD = response.body().getMq0701dFormreq319();
                    if (datosProductoD == null){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SegundaPantalla.this, "No hay información a recuperar", Toast.LENGTH_LONG).show();
                    }else {
                        listDatosD = new ArrayList();
                        if (datosProductoD.getRowset().size()==1){
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        for (int i = 0; i < datosProductoD.getRowset().size()-1; i++) {
                            ArrayList<RowsetD> rowsetD = (ArrayList<RowsetD>) datosProductoD.getRowset();
                            String numeroLote = rowsetD.get(i).getNumeroLoteSerie();
                            String ps = rowsetD.get(i).getPs();
                            String ubicacion2 = rowsetD.get(i).getUbicacion();
                            Integer existenciasFisicas = rowsetD.get(i).getExistFisicas();
                            Integer comprometido = rowsetD.get(i).getComprometido();
                            Integer disponible = rowsetD.get(i).getDisponible();
                            listDatosD.add(ps);
                            ubicacion.append(ubicacion2+"\n");
                           // lote.append(numeroLote+"\n");
                            exis.append(""+existenciasFisicas+"\n");
                            compr.append(""+comprometido+"\n");
                            disp.append(""+disponible+"\n");
                            SegundoAdapterDatos adapter = new SegundoAdapterDatos(listDatosD);
                            recyclerD.setAdapter(adapter);

                            if(numeroLote.equals("null")){
                                lote.append("");
                            }else{
                                lote.append(numeroLote+"\n");

                            }
                          /*  if(ubicacion2.equals("TOTAL:")){
                                ubicacion.append("");
                            }else{
                                ubicacion.append(ubicacion2+"\n");

                            }*/
                            progressBar.setVisibility(View.INVISIBLE);


                        }
                    }
                }else{
                    Toast.makeText(SegundaPantalla.this, "Error  al obtener los datos", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<CuerpoD> call, Throwable t) {
                Toast.makeText(SegundaPantalla.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void Volver(View v){
        Intent e = new Intent(SegundaPantalla.this,PantallaPrincipal.class);
        startActivity(e);
        borrarGlobal =1;

    }
}



