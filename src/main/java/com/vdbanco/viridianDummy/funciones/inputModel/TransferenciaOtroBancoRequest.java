package com.vdbanco.viridianDummy.funciones.inputModel;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(name = "TransferenciaOtroBancoRequest")
public class TransferenciaOtroBancoRequest {

    @ApiObjectField(name = "accountNumberOrigen")
    private String accountNumberOrigen;
    @ApiObjectField(name ="accountNumberDestino")
    private String accountNumberDestino;
    @ApiObjectField(name ="nombreDestinatario")
    private String nombreDestinatario;
    @ApiObjectField(name ="nombreBancoDestino")
    private String nombreBancoDestino;
    @ApiObjectField(name ="numeroBancoDestino")
    private String numeroBancoDestino;
    @ApiObjectField(name ="monto")
    private Double monto;
    @ApiObjectField(name ="glossa")
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

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getNombreBancoDestino() {
        return nombreBancoDestino;
    }

    public void setNombreBancoDestino(String nombreBancoDestino) {
        this.nombreBancoDestino = nombreBancoDestino;
    }

    public String getNumeroBancoDestino() {
        return numeroBancoDestino;
    }

    public void setNumeroBancoDestino(String numeroBancoDestino) {
        this.numeroBancoDestino = numeroBancoDestino;
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
