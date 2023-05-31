package com.quantum.consultadisponibilidad.parseoMQ0701B;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RowsetB{

    @SerializedName("Existencias")
    @Expose
    private Integer existencias;
    @SerializedName("Deposito")
    @Expose
    private String deposito;
    @SerializedName("Item_Codigo")
    @Expose
    private String itemCodigo;
    @SerializedName("Item_Descripcion")
    @Expose
    private String itemDescripcion;
    @SerializedName("UM")
    @Expose
    private String um;

    public Integer getExistencias() {
        return existencias;
    }

    public void setExistencias(Integer existencias) {
        this.existencias = existencias;
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

    public String getItemDescripcion() {
        return itemDescripcion;
    }

    public void setItemDescripcion(String itemDescripcion) {
        this.itemDescripcion = itemDescripcion;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

}
