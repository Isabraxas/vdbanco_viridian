package com.vdbanco.viridianDummy.funciones.inputModel;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiObject(name = "TransferenciaOtroBancoRequest")
public class TransferenciaOtroBancoRequest {

    @ApiObjectField(name = "accountNumberOrigen")
    @NotBlank
    private String accountNumberOrigen;
    @ApiObjectField(name ="accountNumberDestino")
    @NotBlank
    private String accountNumberDestino;
    @ApiObjectField(name ="nombreDestinatario")
    @NotBlank
    private String nombreDestinatario;
    @ApiObjectField(name ="nombreBancoDestino")
    @NotBlank
    private String nombreBancoDestino;
    @ApiObjectField(name ="numeroBancoDestino")
    @NotBlank
    private String numeroBancoDestino;
    @ApiObjectField(name ="monto")
    @NotNull
    @Min(value = 1)
    private Double monto;
    @ApiObjectField(name ="glossa")
    @NotBlank
    private String glossa;
    @ApiObjectField(name ="autorizacionNumber")
    @NotBlank
    @Size(min=5)
    private String autorizacionNumber;


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

    public String getAutorizacionNumber() {
        return autorizacionNumber;
    }

    public void setAutorizacionNumber(String autorizacionNumber) {
        this.autorizacionNumber = autorizacionNumber;
    }
}
