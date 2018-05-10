package com.vdbanco.viridianDummy.error;

import java.sql.Timestamp;

public class ErrorSaldoInsuficiente {

    private Timestamp fecha;
    private String estado;
    private ErrorDetalle error;


    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ErrorDetalle getError() {
        return error;
    }

    public void setError(ErrorDetalle error) {
        this.error = error;
    }
}
