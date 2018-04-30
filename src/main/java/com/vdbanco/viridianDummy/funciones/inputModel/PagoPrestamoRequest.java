package com.vdbanco.viridianDummy.funciones.inputModel;

public class PagoPrestamoRequest {

    private String accountNumberOrigen;
    private String accountNumberDestino;
    private Double monto;
    private String glossa;


    public String getAccountNumberOrigen() {
        return accountNumberOrigen;
    }

    public void setAccountNumberOrigen(String accountNumberOrigen) {
        this.accountNumberOrigen = accountNumberOrigen;
    }

    public String getAccountNumberDestino() {
        return accountNumberDestino;
    }

    public void setAccountNumberDestino(String accountNumberDestino) {
        this.accountNumberDestino = accountNumberDestino;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getGlossa() {
        return glossa;
    }

    public void setGlossa(String glossa) {
        this.glossa = glossa;
    }
}
