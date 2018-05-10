package com.vdbanco.viridianDummy.error;

/**
 * Created by marcelo on 09-03-18
 * Clase para generalizar los mensajes de error
 */
public class EntidadError {

    protected Long id;
    protected String estado;
    protected ErrorDetalle error;

    public EntidadError() {
    }

    public EntidadError(Long id, String estado, ErrorDetalle error) {
        this.id = id;
        this.estado = estado;
        this.error = error;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
