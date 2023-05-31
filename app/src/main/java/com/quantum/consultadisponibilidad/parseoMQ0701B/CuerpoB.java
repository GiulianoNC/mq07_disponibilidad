package com.quantum.consultadisponibilidad.parseoMQ0701B;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quantum.consultadisponibilidad.parseoMQ0701A.Mq0701aData;

public class CuerpoB {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("Item_Codigo")
    @Expose
    private String itemCodigo;
    @SerializedName("MQ0701B_DATA")
    @Expose
    private Mq0701bData mq0701bData;
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

    public String getItemCodigo() {
        return itemCodigo;
    }

    public void setItemCodigo(String itemCodigo) {
        this.itemCodigo = itemCodigo;
    }

    public Mq0701bData getMq0701bData() {
        return mq0701bData;
    }

    public void setMq0701bData(Mq0701bData mq0701bData) {
        this.mq0701bData = mq0701bData;
    }

    public String getJdeStatus() {
        return jdeStatus;
    }

    public void setJdeStatus(String jdeStatus) {
        this.jdeStatus = jdeStatus;
    }

    public CuerpoB(String username, String password, String item) {
        this.username = username;
        this.password = password;
        this.itemCodigo = item;
    }
}
