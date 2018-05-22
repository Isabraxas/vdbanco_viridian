package com.vdbanco.viridianDummy.funciones.inputModel;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiObject(name = "PagoPrestamoRequest")
public class PagoPrestamoRequest implements Serializable{

    @ApiObjectField(name = "accountNumberOrigen", required = true)
    @NotBlank
    private String accountNumberOrigen;
    @ApiObjectField(name = "accountNumberDestino", required = true)
    @NotBlank
    private String accountNumberDestino;
    @ApiObjectField(name = "monto", required = true)
    @NotNull
    private Double monto;
    @ApiObjectField(name = "glossa", required = true)
    @NotBlank
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
