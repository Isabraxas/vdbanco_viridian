package com.vdbanco.viridianDummy.funciones.inputModel;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiObject(name = "TransferenciaTerceroRequest")
public class TransferenciaTerceroRequest {

    @ApiObjectField
    @NotBlank
    private String accountNumberOrigen;
    @ApiObjectField
    @NotBlank
    private String accountNumberDestino;
    @ApiObjectField
    @NotBlank
    private String nombreDestinatario;
    @ApiObjectField
    @NotBlank
    private Double monto;
    @ApiObjectField
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
