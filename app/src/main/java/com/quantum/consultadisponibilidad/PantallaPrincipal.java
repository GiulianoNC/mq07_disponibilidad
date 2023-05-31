package com.quantum.consultadisponibilidad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quantum.consultadisponibilidad.adaptador.AdapterDatos;
import com.quantum.consultadisponibilidad.conectividad.Conexion;
import com.quantum.consultadisponibilidad.parseoMQ0701A.CuerpoA;
import com.quantum.consultadisponibilidad.parseoMQ0701A.Mq0701aData;
import com.quantum.consultadisponibilidad.parseoMQ0701A.RowsetA;
import com.quantum.consultadisponibilidad.parseoMQ0701B.CuerpoB;
import com.quantum.consultadisponibilidad.parseoMQ0701B.Mq0701bData;
import com.quantum.consultadisponibilidad.parseoMQ0701B.RowsetB;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.quantum.consultadisponibilidad.LoginActivity.claveGlobal;
import static com.quantum.consultadisponibilidad.LoginActivity.usuarioGlobal;
import static com.quantum.consultadisponibilidad.Configuracion.direc;
import static com.quantum.consultadisponibilidad.SegundaPantalla.borrarGlobal;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PantallaPrincipal extends AppCompatActivity {

    public static String codItemGlobal = null;
    public static String descItemGlobal = null;
    public static String plantaGlobal = null;
    public static String UMGlobal = null;
    public static String ItemGlobal = null;

    public static boolean CheckBoxGlobal = false;




    public static String existenciasGlobal = null;
    private TextView item, codItem, descripcion, sucursal, existencias;
    private TextView qtm;
    private CheckBox ckbxCodItem;
    private TextView datosUM;
    private ProgressBar progressBar;
    RecyclerView recycler;
    ArrayList<String> listDatosA;
    ArrayList<String> listDatosB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);



        item = findViewById(R.id.itemABuscar);
        ckbxCodItem = findViewById(R.id.checkBoxCodItem);
        qtm = findViewById(R.id.QTMtitulo);
        qtm.setText("QTM - DISPONIBILIDAD" );
        datosUM = findViewById(R.id.datosUM);
        descripcion = findViewById(R.id.descripcion);
        sucursal = findViewById(R.id.sucursal);
        existencias = findViewById(R.id.existencias);
        codItem = findViewById(R.id.codigoItem);
        recycler= (RecyclerView) findViewById(R.id.itemRecycler);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        //recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        manager.setOrientation(RecyclerView.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setHasFixedSize(true);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //statusBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(Color.rgb(102,45,145));  //Define color


        if(borrarGlobal == 1){
            Limpiar2();
        }

        if(ItemGlobal != null){
            buscoItem2();
        }

    }

    public void buscoItem(View view){
        String itemSel = item.getText().toString();

        ItemGlobal = item.getText().toString();



        recycler.setVisibility(View.VISIBLE);
        Limpiar2();
        if (ckbxCodItem.isChecked()==false){
            progressBar.setVisibility(View.VISIBLE);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(direc)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Conexion conexion = retrofit.create(Conexion.class);
            CuerpoA cuerpoA = new CuerpoA(usuarioGlobal,claveGlobal,itemSel);

            Call<CuerpoA> call = conexion.getDataA(cuerpoA);
            call.enqueue(new Callback<CuerpoA>() {
                @Override
                public void onResponse(Call<CuerpoA> call, Response<CuerpoA> response) {
                    int statusCode = response.code();

                    if (statusCode == 200) {

                        //CuerpoA cuerpo =  response.body();



                        Mq0701aData listadoProductosA = response.body().getMq0701aData();


                        int records = listadoProductosA.getRecords() ;

                        if (records == 0){
                            Toast.makeText(PantallaPrincipal.this, "No se encontró coincidencias en la busqueda " , Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }


                        listDatosA = new ArrayList();
                        for (int i = 0; i < listadoProductosA.getRowset().size(); i++) {
                            ArrayList<RowsetA> rowsetA = (ArrayList<RowsetA>) listadoProductosA.getRowset();
                            String itemTabla = rowsetA.get(i).getItemCodigo();
                            String descripcion2 = rowsetA.get(i).getItemDescripcion();
                            String uMed = rowsetA.get(i).getUm();
                            String sucursal2 = rowsetA.get(i).getDesposito();
                            Integer existencias2 = rowsetA.get(i).getExistencias();
                            listDatosA.add(itemTabla);
                            datosUM.append(uMed + "\n\n\n\n");
                            descripcion.append(descripcion2 + "\n\n\n\n");
                            sucursal.append(sucursal2 + "\n\n\n\n");
                            existencias.append(existencias2+"\n\n\n\n");
                            AdapterDatos adapter = new AdapterDatos(listDatosA);
                            recycler.setAdapter(adapter);
                            if (listDatosA != null){
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            adapter.setOnclickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int posicion = recycler.indexOfChild(view);
                                    codItem.setText(listDatosA.get(recycler.getChildAdapterPosition(view)));
                                    String codItem2 = codItem.getText().toString();
                                    descripcion.setText(rowsetA.get(posicion).getItemDescripcion());
                                    String descripcion2 = descripcion.getText().toString();
                                    datosUM.setText(rowsetA.get(posicion).getUm());
                                    String datosUM2 = datosUM.getText().toString();
                                    sucursal.setText(rowsetA.get(posicion).getDesposito());
                                    String sucursal2 = sucursal.getText().toString();
                                    existencias.setText(rowsetA.get(posicion).getExistencias().toString());
                                    String existencias2 = existencias.getText().toString();
                                    progressBar.setVisibility(View.VISIBLE);

                                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                            .readTimeout(120, TimeUnit.SECONDS)
                                            .connectTimeout(120, TimeUnit.SECONDS)
                                            .build();
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(direc)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .client(okHttpClient)
                                            .build();
                                    Conexion conexion = retrofit.create(Conexion.class);
                                    CuerpoA cuerpoA = new CuerpoA(usuarioGlobal,claveGlobal,itemSel);
                                    Call<CuerpoA> call = conexion.getDataA(cuerpoA);
                                    call.enqueue(new Callback<CuerpoA>() {
                                        @Override
                                        public void onResponse(Call<CuerpoA> call, Response<CuerpoA> response) {
                                            int statusCode = response.code();

                                            if (statusCode == 200) {
                                                Mq0701aData listadoProductosA = response.body().getMq0701aData();
                                                listDatosA = new ArrayList();
                                                for (int i = 0; i < listadoProductosA.getRowset().size(); i++) {
                                                    ArrayList<RowsetA> rowsetA = (ArrayList<RowsetA>) listadoProductosA.getRowset();
                                                    if (rowsetA.get(i).getItemCodigo().equals(codItem2)
                                                            &&(rowsetA.get(i).getItemDescripcion().equals(descripcion2))
                                                            &&(rowsetA.get(i).getUm().equals(datosUM2))
                                                            &&(rowsetA.get(i).getDesposito().equals(sucursal2))
                                                    ){
                                                        codItemGlobal=rowsetA.get(i).getItemCodigo();
                                                        descItemGlobal=rowsetA.get(i).getItemDescripcion();
                                                        plantaGlobal=rowsetA.get(i).getDesposito();
                                                        UMGlobal=rowsetA.get(i).getUm();
                                                        existenciasGlobal=rowsetA.get(i).getExistencias().toString();

                                                        Intent siguiente = new Intent(PantallaPrincipal.this, SegundaPantalla.class);
                                                        progressBar.setVisibility(View.INVISIBLE);

                                                        recycler.setVisibility(View.INVISIBLE);
                                                        item.setText("");
                                                        codItem.setText("");
                                                        sucursal.setText("");
                                                        existencias.setText("");
                                                        listDatosA = new ArrayList();
                                                        listDatosA.clear();
                                                        listDatosB = new ArrayList<>();
                                                        listDatosB.clear();
                                                        datosUM.setText("");
                                                        descripcion.setText("");
                                                        recycler.setVisibility(View.INVISIBLE);
                                                        if (ckbxCodItem.isChecked()==true){
                                                            ckbxCodItem.setChecked(false);
                                                        }
                                                        codItem.append("");
                                                        item.append("");
                                                        sucursal.append("");
                                                        existencias.append("");
                                                        datosUM.append("");
                                                        descripcion.append("");

                                                        startActivity(siguiente);
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(PantallaPrincipal.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }

                                        @Override
                                        public void onFailure(Call<CuerpoA> call, Throwable t) {
                                            Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                                        }

                                    });
                                }
                            });
                        }
                    }
                    if(statusCode != 200){
                        Toast.makeText(PantallaPrincipal.this, "Error  al obtener los datos", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onFailure(Call<CuerpoA> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(PantallaPrincipal.this, "No se encontró coincidencias", Toast.LENGTH_LONG).show();


                    //agrego por giuli
                    if (itemSel.length() == 00){
                        Toast.makeText(PantallaPrincipal.this, "completar el campo de busqueda", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    }else{
                        Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                }
            });
        }
        else{
            if (ckbxCodItem.isChecked()==true){

                CheckBoxGlobal = true;

                progressBar.setVisibility(View.VISIBLE);
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(120, TimeUnit.SECONDS)
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(direc)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Conexion conexion = retrofit.create(Conexion.class);
                CuerpoB cuerpoB = new CuerpoB(usuarioGlobal,claveGlobal,itemSel);

                Call<CuerpoB> call = conexion.getDataB(cuerpoB);
                call.enqueue(new Callback<CuerpoB>() {
                    @Override
                    public void onResponse(Call<CuerpoB> call, Response<CuerpoB> response) {
                        int statusCode = response.code();
                        if (statusCode == 200) {



                            Mq0701bData listadoProductosB = response.body().getMq0701bData();

                            int records = listadoProductosB.getRecords() ;

                            if (records == 0){
                                Toast.makeText(PantallaPrincipal.this, "No se encontró coincidencias en la busqueda " , Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                            }

                            listDatosB = new ArrayList();
                            for (int i = 0; i < listadoProductosB.getRowset().size(); i++) {
                                ArrayList<RowsetB> rowsetB = (ArrayList<RowsetB>) listadoProductosB.getRowset();
                                String itemTabla = rowsetB.get(i).getItemCodigo();
                                String descripcion2 = rowsetB.get(i).getItemDescripcion();
                                String uMed = rowsetB.get(i).getUm();
                                String sucursal2 = rowsetB.get(i).getDeposito();
                                Integer existencias2 = rowsetB.get(i).getExistencias();
                                listDatosB.add(itemTabla);
                                datosUM.append(uMed + "\n\n\n\n");
                                descripcion.append(descripcion2 + "\n\n\n\n");
                                sucursal.append(sucursal2 + "\n\n\n\n");
                                existencias.append(existencias2+"\n\n\n\n");
                                AdapterDatos adapter = new AdapterDatos(listDatosB);
                                recycler.setAdapter(adapter);
                                if (listDatosB != null){
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                adapter.setOnclickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int posicion=recycler.indexOfChild(view);
                                        codItem.setText(listDatosB.get(recycler.getChildAdapterPosition(view)));
                                        String codItem2 = codItem.getText().toString();
                                        descripcion.setText(rowsetB.get(posicion).getItemDescripcion());
                                        String descripcion2 = descripcion.getText().toString();
                                        datosUM.setText(rowsetB.get(posicion).getUm());
                                        String datosUM2 = datosUM.getText().toString();
                                        sucursal.setText(rowsetB.get(posicion).getDeposito());
                                        String sucursal2 = sucursal.getText().toString();
                                        existencias.setText(rowsetB.get(posicion).getExistencias().toString());
                                        String existencias2 = existencias.getText().toString();
                                        progressBar.setVisibility(View.VISIBLE);

                                        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                                .readTimeout(120, TimeUnit.SECONDS)
                                                .connectTimeout(120, TimeUnit.SECONDS)
                                                .build();
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(direc)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .client(okHttpClient)
                                                .build();
                                        Conexion conexion = retrofit.create(Conexion.class);
                                        CuerpoB cuerpoB = new CuerpoB(usuarioGlobal,claveGlobal,itemSel);
                                        Call<CuerpoB> call = conexion.getDataB(cuerpoB);
                                        call.enqueue(new Callback<CuerpoB>() {
                                            @Override
                                            public void onResponse(Call<CuerpoB> call, Response<CuerpoB> response) {
                                                int statusCode = response.code();
                                                if (statusCode == 200) {
                                                    Mq0701bData listadoProductosB = response.body().getMq0701bData();
                                                    listDatosB = new ArrayList();
                                                    for (int i = 0; i < listadoProductosB.getRowset().size(); i++) {
                                                        ArrayList<RowsetB> rowsetB = (ArrayList<RowsetB>) listadoProductosB.getRowset();
                                                        if (rowsetB.get(i).getItemCodigo().equals(codItem2)
                                                                &&(rowsetB.get(i).getItemDescripcion().equals(descripcion2))
                                                                &&(rowsetB.get(i).getUm().equals(datosUM2))
                                                                &&(rowsetB.get(i).getDeposito().equals(sucursal2))
                                                        ){
                                                            codItemGlobal=rowsetB.get(i).getItemCodigo();
                                                            descItemGlobal=rowsetB.get(i).getItemDescripcion();
                                                            plantaGlobal=rowsetB.get(i).getDeposito();
                                                            UMGlobal=rowsetB.get(i).getUm();
                                                            existenciasGlobal=rowsetB.get(i).getExistencias().toString();
                                                            Intent siguiente = new Intent(PantallaPrincipal.this, SegundaPantalla.class);

                                                            progressBar.setVisibility(View.INVISIBLE);

                                                            recycler.setVisibility(View.INVISIBLE);
                                                            item.setText("");
                                                            codItem.setText("");
                                                            sucursal.setText("");
                                                            existencias.setText("");
                                                            listDatosA = new ArrayList();
                                                            listDatosA.clear();
                                                            listDatosB = new ArrayList<>();
                                                            listDatosB.clear();
                                                            datosUM.setText("");
                                                            descripcion.setText("");
                                                            recycler.setVisibility(View.INVISIBLE);
                                                            if (ckbxCodItem.isChecked()==true){
                                                                ckbxCodItem.setChecked(false);
                                                            }
                                                            codItem.append("");
                                                            item.append("");
                                                            sucursal.append("");
                                                            existencias.append("");
                                                            datosUM.append("");
                                                            descripcion.append("");

                                                            startActivity(siguiente);
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(PantallaPrincipal.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<CuerpoB> call, Throwable t) {
                                                Toast.makeText(PantallaPrincipal.this, "Hubo una falla  al obtener los datos", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.INVISIBLE);

                                            }
                                        });
                                    }
                                });
                            }
                        }
                        if(statusCode != 200){
                            Toast.makeText(PantallaPrincipal.this, "Error  al obtener los datos", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }

                    public void onFailure(Call<CuerpoB> call, Throwable t) {
                        Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }
    }

    public void Siguiente (View view){
        Intent e = new Intent(PantallaPrincipal.this,LoginActivity.class);
        startActivity(e);
    }

    public void Salir(View v){
        new AlertDialog.Builder(this)
                .setTitle("¿Realmente desea cerrar la aplicación?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //android.os.Process.killProcess(android.os.Process.myPid()); //Su funcion es algo similar a lo que se llama cuando se presiona el botón "Forzar Detención" o "Administrar aplicaciones", lo cuál mata la aplicación
                        finishAffinity();;
                    }
                }).show();
    }

    public void Limpiar(View v){

        item.setText("");
        codItem.setText("");
        sucursal.setText("");
        existencias.setText("");
        listDatosA = new ArrayList();
        listDatosA.clear();
        listDatosB = new ArrayList<>();
        listDatosB.clear();
        datosUM.setText("");
        descripcion.setText("");
        recycler.setVisibility(View.INVISIBLE);
        if (ckbxCodItem.isChecked()==true){
            ckbxCodItem.setChecked(false);
        }
        codItem.append("");
        item.append("");
        sucursal.append("");
        existencias.append("");
        datosUM.append("");
        descripcion.append("");
    }

    public void buscoItem2(){

        if(ItemGlobal.length() != 0 ){
        String itemSel = ItemGlobal;


        recycler.setVisibility(View.VISIBLE);
        Limpiar2();
        if ( CheckBoxGlobal ==false){
            progressBar.setVisibility(View.VISIBLE);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(direc)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Conexion conexion = retrofit.create(Conexion.class);
            CuerpoA cuerpoA = new CuerpoA(usuarioGlobal,claveGlobal,itemSel);

            Call<CuerpoA> call = conexion.getDataA(cuerpoA);
            call.enqueue(new Callback<CuerpoA>() {
                @Override
                public void onResponse(Call<CuerpoA> call, Response<CuerpoA> response) {
                    int statusCode = response.code();

                    if (statusCode == 200) {

                        //CuerpoA cuerpo =  response.body();



                        Mq0701aData listadoProductosA = response.body().getMq0701aData();


                        int records = listadoProductosA.getRecords() ;

                        if (records == 0){
                            Toast.makeText(PantallaPrincipal.this, "No se encontró coincidencias en la busqueda " , Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }


                        listDatosA = new ArrayList();
                        for (int i = 0; i < listadoProductosA.getRowset().size(); i++) {
                            ArrayList<RowsetA> rowsetA = (ArrayList<RowsetA>) listadoProductosA.getRowset();
                            String itemTabla = rowsetA.get(i).getItemCodigo();
                            String descripcion2 = rowsetA.get(i).getItemDescripcion();
                            String uMed = rowsetA.get(i).getUm();
                            String sucursal2 = rowsetA.get(i).getDesposito();
                            Integer existencias2 = rowsetA.get(i).getExistencias();
                            listDatosA.add(itemTabla);
                            datosUM.append(uMed + "\n\n\n\n");
                            descripcion.append(descripcion2 + "\n\n\n\n");
                            sucursal.append(sucursal2 + "\n\n\n\n");
                            existencias.append(existencias2+"\n\n\n\n");
                            AdapterDatos adapter = new AdapterDatos(listDatosA);
                            recycler.setAdapter(adapter);
                            if (listDatosA != null){
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            adapter.setOnclickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int posicion = recycler.indexOfChild(view);
                                    codItem.setText(listDatosA.get(recycler.getChildAdapterPosition(view)));
                                    String codItem2 = codItem.getText().toString();
                                    descripcion.setText(rowsetA.get(posicion).getItemDescripcion());
                                    String descripcion2 = descripcion.getText().toString();
                                    datosUM.setText(rowsetA.get(posicion).getUm());
                                    String datosUM2 = datosUM.getText().toString();
                                    sucursal.setText(rowsetA.get(posicion).getDesposito());
                                    String sucursal2 = sucursal.getText().toString();
                                    existencias.setText(rowsetA.get(posicion).getExistencias().toString());
                                    String existencias2 = existencias.getText().toString();
                                    progressBar.setVisibility(View.VISIBLE);

                                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                            .readTimeout(120, TimeUnit.SECONDS)
                                            .connectTimeout(120, TimeUnit.SECONDS)
                                            .build();
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(direc)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .client(okHttpClient)
                                            .build();
                                    Conexion conexion = retrofit.create(Conexion.class);
                                    CuerpoA cuerpoA = new CuerpoA(usuarioGlobal,claveGlobal,itemSel);
                                    Call<CuerpoA> call = conexion.getDataA(cuerpoA);
                                    call.enqueue(new Callback<CuerpoA>() {
                                        @Override
                                        public void onResponse(Call<CuerpoA> call, Response<CuerpoA> response) {
                                            int statusCode = response.code();

                                            if (statusCode == 200) {
                                                Mq0701aData listadoProductosA = response.body().getMq0701aData();
                                                listDatosA = new ArrayList();
                                                for (int i = 0; i < listadoProductosA.getRowset().size(); i++) {
                                                    ArrayList<RowsetA> rowsetA = (ArrayList<RowsetA>) listadoProductosA.getRowset();
                                                    if (rowsetA.get(i).getItemCodigo().equals(codItem2)
                                                            &&(rowsetA.get(i).getItemDescripcion().equals(descripcion2))
                                                            &&(rowsetA.get(i).getUm().equals(datosUM2))
                                                            &&(rowsetA.get(i).getDesposito().equals(sucursal2))
                                                    ){
                                                        codItemGlobal=rowsetA.get(i).getItemCodigo();
                                                        descItemGlobal=rowsetA.get(i).getItemDescripcion();
                                                        plantaGlobal=rowsetA.get(i).getDesposito();
                                                        UMGlobal=rowsetA.get(i).getUm();
                                                        existenciasGlobal=rowsetA.get(i).getExistencias().toString();

                                                        Intent siguiente = new Intent(PantallaPrincipal.this, SegundaPantalla.class);
                                                        progressBar.setVisibility(View.INVISIBLE);

                                                        recycler.setVisibility(View.INVISIBLE);
                                                        item.setText("");
                                                        codItem.setText("");
                                                        sucursal.setText("");
                                                        existencias.setText("");
                                                        listDatosA = new ArrayList();
                                                        listDatosA.clear();
                                                        listDatosB = new ArrayList<>();
                                                        listDatosB.clear();
                                                        datosUM.setText("");
                                                        descripcion.setText("");
                                                        recycler.setVisibility(View.INVISIBLE);
                                                        if (ckbxCodItem.isChecked()==true){
                                                            ckbxCodItem.setChecked(false);
                                                        }
                                                        codItem.append("");
                                                        item.append("");
                                                        sucursal.append("");
                                                        existencias.append("");
                                                        datosUM.append("");
                                                        descripcion.append("");

                                                        startActivity(siguiente);
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(PantallaPrincipal.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }

                                        @Override
                                        public void onFailure(Call<CuerpoA> call, Throwable t) {
                                            Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                                        }

                                    });
                                }
                            });
                        }
                    }
                    if(statusCode != 200){
                        Toast.makeText(PantallaPrincipal.this, "Error  al obtener los datos", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onFailure(Call<CuerpoA> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(PantallaPrincipal.this, "No se encontró coincidencias", Toast.LENGTH_LONG).show();


                    //agrego por giuli
                    if (itemSel.length() == 00){
                        Toast.makeText(PantallaPrincipal.this, "completar el campo de busqueda", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    }else{
                        Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    }

                }
            });
        }
        else{
            if (CheckBoxGlobal == true){
                progressBar.setVisibility(View.VISIBLE);
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(120, TimeUnit.SECONDS)
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(direc)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Conexion conexion = retrofit.create(Conexion.class);
                CuerpoB cuerpoB = new CuerpoB(usuarioGlobal,claveGlobal,itemSel);

                Call<CuerpoB> call = conexion.getDataB(cuerpoB);
                call.enqueue(new Callback<CuerpoB>() {
                    @Override
                    public void onResponse(Call<CuerpoB> call, Response<CuerpoB> response) {
                        int statusCode = response.code();
                        if (statusCode == 200) {



                            Mq0701bData listadoProductosB = response.body().getMq0701bData();

                            int records = listadoProductosB.getRecords() ;

                            if (records == 0){
                                Toast.makeText(PantallaPrincipal.this, "No se encontró coincidencias en la busqueda " , Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);

                            }

                            listDatosB = new ArrayList();
                            for (int i = 0; i < listadoProductosB.getRowset().size(); i++) {
                                ArrayList<RowsetB> rowsetB = (ArrayList<RowsetB>) listadoProductosB.getRowset();
                                String itemTabla = rowsetB.get(i).getItemCodigo();
                                String descripcion2 = rowsetB.get(i).getItemDescripcion();
                                String uMed = rowsetB.get(i).getUm();
                                String sucursal2 = rowsetB.get(i).getDeposito();
                                Integer existencias2 = rowsetB.get(i).getExistencias();
                                listDatosB.add(itemTabla);
                                datosUM.append(uMed + "\n\n\n\n");
                                descripcion.append(descripcion2 + "\n\n\n\n");
                                sucursal.append(sucursal2 + "\n\n\n\n");
                                existencias.append(existencias2+"\n\n\n\n");
                                AdapterDatos adapter = new AdapterDatos(listDatosB);
                                recycler.setAdapter(adapter);
                                if (listDatosB != null){
                                    progressBar.setVisibility(View.INVISIBLE);
                                }

                                adapter.setOnclickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int posicion=recycler.indexOfChild(view);
                                        codItem.setText(listDatosB.get(recycler.getChildAdapterPosition(view)));
                                        String codItem2 = codItem.getText().toString();
                                        descripcion.setText(rowsetB.get(posicion).getItemDescripcion());
                                        String descripcion2 = descripcion.getText().toString();
                                        datosUM.setText(rowsetB.get(posicion).getUm());
                                        String datosUM2 = datosUM.getText().toString();
                                        sucursal.setText(rowsetB.get(posicion).getDeposito());
                                        String sucursal2 = sucursal.getText().toString();
                                        existencias.setText(rowsetB.get(posicion).getExistencias().toString());
                                        String existencias2 = existencias.getText().toString();
                                        progressBar.setVisibility(View.VISIBLE);

                                        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                                .readTimeout(120, TimeUnit.SECONDS)
                                                .connectTimeout(120, TimeUnit.SECONDS)
                                                .build();
                                        Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(direc)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .client(okHttpClient)
                                                .build();
                                        Conexion conexion = retrofit.create(Conexion.class);
                                        CuerpoB cuerpoB = new CuerpoB(usuarioGlobal,claveGlobal,itemSel);
                                        Call<CuerpoB> call = conexion.getDataB(cuerpoB);
                                        call.enqueue(new Callback<CuerpoB>() {
                                            @Override
                                            public void onResponse(Call<CuerpoB> call, Response<CuerpoB> response) {
                                                int statusCode = response.code();
                                                if (statusCode == 200) {
                                                    Mq0701bData listadoProductosB = response.body().getMq0701bData();
                                                    listDatosB = new ArrayList();
                                                    for (int i = 0; i < listadoProductosB.getRowset().size(); i++) {
                                                        ArrayList<RowsetB> rowsetB = (ArrayList<RowsetB>) listadoProductosB.getRowset();
                                                        if (rowsetB.get(i).getItemCodigo().equals(codItem2)
                                                                &&(rowsetB.get(i).getItemDescripcion().equals(descripcion2))
                                                                &&(rowsetB.get(i).getUm().equals(datosUM2))
                                                                &&(rowsetB.get(i).getDeposito().equals(sucursal2))
                                                        ){
                                                            codItemGlobal=rowsetB.get(i).getItemCodigo();
                                                            descItemGlobal=rowsetB.get(i).getItemDescripcion();
                                                            plantaGlobal=rowsetB.get(i).getDeposito();
                                                            UMGlobal=rowsetB.get(i).getUm();
                                                            existenciasGlobal=rowsetB.get(i).getExistencias().toString();
                                                            Intent siguiente = new Intent(PantallaPrincipal.this, SegundaPantalla.class);

                                                            progressBar.setVisibility(View.INVISIBLE);

                                                            recycler.setVisibility(View.INVISIBLE);
                                                            item.setText("");
                                                            codItem.setText("");
                                                            sucursal.setText("");
                                                            existencias.setText("");
                                                            listDatosA = new ArrayList();
                                                            listDatosA.clear();
                                                            listDatosB = new ArrayList<>();
                                                            listDatosB.clear();
                                                            datosUM.setText("");
                                                            descripcion.setText("");
                                                            recycler.setVisibility(View.INVISIBLE);
                                                            if (ckbxCodItem.isChecked()==true){
                                                                ckbxCodItem.setChecked(false);
                                                            }
                                                            codItem.append("");
                                                            item.append("");
                                                            sucursal.append("");
                                                            existencias.append("");
                                                            datosUM.append("");
                                                            descripcion.append("");

                                                            startActivity(siguiente);
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(PantallaPrincipal.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<CuerpoB> call, Throwable t) {
                                                Toast.makeText(PantallaPrincipal.this, "Hubo una falla  al obtener los datos", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.INVISIBLE);

                                            }
                                        });
                                    }
                                });
                            }
                        }
                        if(statusCode != 200){
                            Toast.makeText(PantallaPrincipal.this, "Error  al obtener los datos", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }

                    public void onFailure(Call<CuerpoB> call, Throwable t) {
                        Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        }
    }else{
            Limpiar2();
        }
    }

    public void Limpiar2(){


        item.setText("");
        codItem.setText("");
        sucursal.setText("");
        existencias.setText("");
        listDatosA = new ArrayList();
        listDatosA.clear();
        listDatosB = new ArrayList<>();
        listDatosB.clear();
        datosUM.setText("");
        descripcion.setText("");
        codItem.append("");
        item.append("");
        sucursal.append("");
        existencias.append("");
        datosUM.append("");
        descripcion.append("");
    }

    public void Limpiar3(){

        recycler.setVisibility(View.INVISIBLE);

        item.setText("");
        codItem.setText("");
        sucursal.setText("");
        existencias.setText("");
        listDatosA = new ArrayList();
        listDatosA.clear();
        listDatosB = new ArrayList<>();
        listDatosB.clear();
        datosUM.setText("");
        descripcion.setText("");
        codItem.append("");
        item.append("");
        sucursal.append("");
        existencias.append("");
        datosUM.append("");
        descripcion.append("");
    }

    //primer pantallazo de app

    public void todo(){
        progressBar.setVisibility(View.VISIBLE);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(direc)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Conexion conexion = retrofit.create(Conexion.class);
        CuerpoA cuerpoA = new CuerpoA(usuarioGlobal,claveGlobal);

        Call<CuerpoA> call = conexion.getDataA(cuerpoA);
        call.enqueue(new Callback<CuerpoA>() {
            @Override
            public void onResponse(Call<CuerpoA> call, Response<CuerpoA> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    Mq0701aData listadoProductosA = response.body().getMq0701aData();
                    listDatosA = new ArrayList();
                    for (int i = 0; i < listadoProductosA.getRowset().size(); i++) {
                        ArrayList<RowsetA> rowsetA = (ArrayList<RowsetA>) listadoProductosA.getRowset();
                        String itemTabla = rowsetA.get(i).getItemCodigo();
                        String descripcion2 = rowsetA.get(i).getItemDescripcion();
                        String uMed = rowsetA.get(i).getUm();
                        String sucursal2 = rowsetA.get(i).getDesposito();
                        Integer existencias2 = rowsetA.get(i).getExistencias();
                        listDatosA.add(itemTabla);
                        datosUM.append(uMed + "\n\n\n\n");
                        descripcion.append(descripcion2 + "\n\n\n\n");
                        sucursal.append(sucursal2 + "\n\n\n\n");
                        existencias.append(existencias2+"\n\n\n\n");
                        AdapterDatos adapter = new AdapterDatos(listDatosA);
                        recycler.setAdapter(adapter);
                        if (listDatosA != null){
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        adapter.setOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int posicion=recycler.indexOfChild(view);
                                codItem.setText(listDatosA.get(recycler.getChildAdapterPosition(view)));
                                String codItem2 = codItem.getText().toString();
                                descripcion.setText(rowsetA.get(posicion).getItemDescripcion());
                                String descripcion2 = descripcion.getText().toString();
                                datosUM.setText(rowsetA.get(posicion).getUm());
                                String datosUM2 = datosUM.getText().toString();
                                sucursal.setText(rowsetA.get(posicion).getDesposito());
                                String sucursal2 = sucursal.getText().toString();
                                existencias.setText(rowsetA.get(posicion).getExistencias().toString());
                                String existencias2 = existencias.getText().toString();
                                progressBar.setVisibility(View.VISIBLE);

                                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                        .readTimeout(120, TimeUnit.SECONDS)
                                        .connectTimeout(120, TimeUnit.SECONDS)
                                        .build();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(direc)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .client(okHttpClient)
                                        .build();
                                Conexion conexion = retrofit.create(Conexion.class);
                                CuerpoA cuerpoA = new CuerpoA(usuarioGlobal,claveGlobal);
                                Call<CuerpoA> call = conexion.getDataA(cuerpoA);
                                call.enqueue(new Callback<CuerpoA>() {
                                    @Override
                                    public void onResponse(Call<CuerpoA> call, Response<CuerpoA> response) {
                                        int statusCode = response.code();
                                        if (statusCode == 200) {
                                            Mq0701aData listadoProductosA = response.body().getMq0701aData();
                                            listDatosA = new ArrayList();
                                            for (int i = 0; i < listadoProductosA.getRowset().size(); i++) {
                                                ArrayList<RowsetA> rowsetA = (ArrayList<RowsetA>) listadoProductosA.getRowset();
                                                if (rowsetA.get(i).getItemCodigo().equals(codItem2)
                                                        &&(rowsetA.get(i).getItemDescripcion().equals(descripcion2))
                                                        &&(rowsetA.get(i).getUm().equals(datosUM2))
                                                        &&(rowsetA.get(i).getDesposito().equals(sucursal2))
                                                ){
                                                    codItemGlobal=rowsetA.get(i).getItemCodigo();
                                                    descItemGlobal=rowsetA.get(i).getItemDescripcion();
                                                    plantaGlobal=rowsetA.get(i).getDesposito();
                                                    UMGlobal=rowsetA.get(i).getUm();
                                                    existenciasGlobal=rowsetA.get(i).getExistencias().toString();
                                                    Intent siguiente = new Intent(PantallaPrincipal.this, SegundaPantalla.class);
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(siguiente);
                                                }
                                            }
                                        } else {
                                            Toast.makeText(PantallaPrincipal.this, "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CuerpoA> call, Throwable t) {
                                        Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.INVISIBLE);

                                    }

                                });
                            }
                        });
                    }
                }
                if(statusCode != 200){
                    Toast.makeText(PantallaPrincipal.this, "Error  al obtener los datos", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);


                }
            }
            @Override
            public void onFailure(Call<CuerpoA> call, Throwable t) {
                Toast.makeText(PantallaPrincipal.this, "Hubo una falla al obtener los datos", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);


            }
        });
    }
}





