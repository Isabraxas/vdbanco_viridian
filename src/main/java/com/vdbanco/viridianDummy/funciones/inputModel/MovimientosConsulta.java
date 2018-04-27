package com.vdbanco.viridianDummy.funciones.inputModel;

import java.sql.Timestamp;

public class MovimientosConsulta {

    private String accountNumber;
    private Timestamp fechaDesde;
    private Timestamp fechaHasta;
    private Integer ultimosMeses;
    private Integer ultimosMovimientos;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Timestamp getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Timestamp fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Timestamp getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Timestamp fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Integer getUltimosMeses() {
        return ultimosMeses;
    }

    public void setUltimosMeses(Integer ultimosMeses) {
        this.ultimosMeses = ultimosMeses;
    }

    public Integer getUltimosMovimientos() {
        return ultimosMovimientos;
    }

    public void setUltimosMovimientos(Integer ultimosMovimientos) {
        this.ultimosMovimientos = ultimosMovimientos;
    }
}
