package com.vdbanco.viridianDummy.funciones.inputModel;

import java.sql.Timestamp;

public class ReversionRequest {
    private String numeroTransacion;
    private String numeroAutorizacion;

    public String getNumeroTransacion() {
        return numeroTransacion;
    }

    public void setNumeroTransacion(String numeroTransacion) {
        this.numeroTransacion = numeroTransacion;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }
}
