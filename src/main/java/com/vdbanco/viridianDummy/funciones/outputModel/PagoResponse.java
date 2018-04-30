package com.vdbanco.viridianDummy.funciones.outputModel;

import com.vdbanco.viridianDummy.domain.TransaccionModel;

import java.sql.Timestamp;

public class PagoResponse {

    private Timestamp fecha;
    private String estado;
    private TransaccionModel detalle;


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

    public TransaccionModel getDetalle() {
        return detalle;
    }

    public void setDetalle(TransaccionModel detalle) {
        this.detalle = detalle;
    }
}
