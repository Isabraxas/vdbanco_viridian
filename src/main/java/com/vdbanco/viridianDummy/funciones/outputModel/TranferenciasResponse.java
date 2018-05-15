package com.vdbanco.viridianDummy.funciones.outputModel;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiObject(name = "TranferenciasResponse",description = "hola")
public class TranferenciasResponse implements Serializable {

    @ApiObjectField(name = "fecha")
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
