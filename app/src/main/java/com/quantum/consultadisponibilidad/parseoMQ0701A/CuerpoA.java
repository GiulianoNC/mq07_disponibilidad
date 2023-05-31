package com.quantum.consultadisponibilidad.parseoMQ0701A;

import android.widget.TextView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CuerpoA {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("Item_Descripcion")
    @Expose
    private String itemDescripcion;
    @SerializedName("MQ0701A_DATA")
    @Expose
    private Mq0701aData mq0701aData;
    @SerializedName("jde__status")
    @Expose
    private String jdeStatus;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getItemDescripcion() {
        return itemDescripcion;
    }

    public void setItemDescripcion(String itemDescripcion) {
        this.itemDescripcion = itemDescripcion;
    }

    public CuerpoA(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public CuerpoA(String username, String password, String item) {
        this.username = username;
        this.password = password;
        this.itemDescripcion = item;
    }

    public Mq0701aData getMq0701aData() {
        return mq0701aData;
    }

    public void setMq0701aData(Mq0701aData mq0701aData) {
        this.mq0701aData = mq0701aData;
    }

    public String getJdeStatus() {
        return jdeStatus;
    }

    public void setJdeStatus(String jdeStatus) {
        this.jdeStatus = jdeStatus;
    }

}

