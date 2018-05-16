package com.vdbanco.viridianDummy.funciones.inputModel;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiObject(name = "TransferenciaPropiaRequest")
public class TransferenciaPropiaRequest {

    @ApiObjectField
    @NotBlank
    private String accountNumberOrigen;
    @ApiObjectField
    @NotBlank
    private String accountNumberDestino;
    @ApiObjectField
    @NotBlank
    @Min(value = 1L)
    private Double monto;
    @ApiObjectField
    @NotBlank
    private String glossa;
    @Size(min=5)
    @NotBlank
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
