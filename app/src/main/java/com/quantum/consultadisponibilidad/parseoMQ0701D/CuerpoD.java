package com.quantum.consultadisponibilidad.parseoMQ0701D;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.quantum.consultadisponibilidad.parseoMQ0701B.Mq0701bData;

public class CuerpoD {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("Deposito")
    @Expose
    private String deposito;
    @SerializedName("Item_Codigo")
    @Expose
    private String itemCodigo;
    @SerializedName("MQ0701D_FORMREQ_319")
    @Expose
    private Mq0701dFormreq319 mq0701dFormreq319;
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

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public String getItemCodigo() {
        return itemCodigo;
    }

    public void setItemCodigo(String itemCodigo) {
        this.itemCodigo = itemCodigo;
    }

    public Mq0701dFormreq319 getMq0701dFormreq319() {
        return mq0701dFormreq319;
    }

    public void setMq0701dFormreq319(Mq0701dFormreq319 mq0701dFormreq319) {
        this.mq0701dFormreq319 = mq0701dFormreq319;
    }

    public String getJdeStatus() {
        return jdeStatus;
    }

    public void setJdeStatus(String jdeStatus) {
        this.jdeStatus = jdeStatus;
    }

    public CuerpoD(String username, String password, String deposito, String itemCodigo) {
        this.username = username;
        this.password = password;
        this.deposito = deposito;
        this.itemCodigo = itemCodigo;
    }
}
