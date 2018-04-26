package com.vdbanco.viridianDummy.error;

/**
 * Created by marcelo on 07-03-18
 */
public class ErrorNoEncontrado {
    private Long id;
    private String codigo;
    private String detalle;
    private String mensaje;

    public ErrorNoEncontrado() {
    }

    public ErrorNoEncontrado(Long id,String codigo, String detalle, String mensaje) {
        this.id = id;
        this.codigo = codigo;
        this.detalle = detalle;
        this.mensaje = mensaje;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
