package com.quantum.consultadisponibilidad.conectividad;

import com.quantum.consultadisponibilidad.parseoMQ0701A.CuerpoA;
import com.quantum.consultadisponibilidad.parseoMQ0701A.Mq0701aData;
import com.quantum.consultadisponibilidad.parseoMQ0701B.CuerpoB;
import com.quantum.consultadisponibilidad.parseoMQ0701B.Mq0701bData;
import com.quantum.consultadisponibilidad.parseoMQ0701D.CuerpoD;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Conexion {

    // seleccion por descripcion del item
    @POST("jderest/v3/orchestrator/MQ0701A_ORCH")
    Call<CuerpoA> getDataA(@Body CuerpoA users );

    // seleccion por codigo del item
    @POST("jderest/v3/orchestrator/MQ0701B_ORCH")
    Call<CuerpoB> getDataB(@Body CuerpoB users );

    @POST("jderest/v3/orchestrator/MQ0701D_ORCH")
    Call<CuerpoD> getDataD(@Body CuerpoD users );

}
