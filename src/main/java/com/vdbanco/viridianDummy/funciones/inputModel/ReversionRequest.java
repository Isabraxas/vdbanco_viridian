package com.vdbanco.viridianDummy.funciones.inputModel;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import java.io.Serializable;

@ApiObject(name = "ReversionRequest", show = true)
public class ReversionRequest implements Serializable{
    @ApiObjectField(name = "numeroTransacion", required = true)
    private String numeroTransacion;
    @ApiObjectField(name = "numeroAutorizacion", required = true)
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
