package com.quantum.consultadisponibilidad.parseoMQ0701D;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RowsetD {

    @SerializedName("Exist_Fisicas")
    @Expose
    private Integer ExistFisicas;
    @SerializedName("Comprometido")
    @Expose
    private Integer Comprometido;
    @SerializedName("Disponible")
    @Expose
    private Integer Disponible;
    @SerializedName("Número lote/ serie")
    @Expose
    private String NumeroLoteSerie;
    @SerializedName("PS")
    @Expose
    private String Ps;
    @SerializedName("Ubicación")
    @Expose
    private String Ubicacion;

    public Integer getExistFisicas() {
        return ExistFisicas;
    }

    public void setExistFisicas(Integer ExistFisicas) {
        this.ExistFisicas = ExistFisicas;
    }

    public Integer getComprometido() {
        return Comprometido;
    }

    public void setComprometido(Integer Comprometido) {
        this.Comprometido = Comprometido;
    }

    public Integer getDisponible() {
        return Disponible;
    }

    public void setDisponible(Integer Disponible) {
        this.Disponible = Disponible;
    }

    public String getNumeroLoteSerie() {
        return NumeroLoteSerie;
    }

    public void setNumeroLoteSerie(String NumeroLoteSerie) {
        this.NumeroLoteSerie = NumeroLoteSerie;
    }

    public String getPs() {
        return Ps;
    }

    public void setPs(String Ps) {
        this.Ps = Ps;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String Ubicacion) {
        this.Ubicacion = Ubicacion;
    }
}
