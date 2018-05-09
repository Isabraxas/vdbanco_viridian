package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name="TRANSACCION")
@Entity
public class TransaccionModel extends ResourceSupport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANS_SEQ")
    @SequenceGenerator(sequenceName = "TRANSACCION_ID_SEQ", allocationSize = 1, name = "TRANS_SEQ")
    private Long transaccionId;
    private String transaccionNumber;
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private Timestamp transaccionDate;
    private Double transaccionMonto;
    private String accountNumber;
    private String transaccionDetalle;
    private String transaccionGlossa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTORIZACION_ID")
    @JsonIgnore
    private AutorizacionModel autorizacion;
    private String autorizacionNumber;


    public Long getTransaccionId() {
        return transaccionId;
    }

    public void setTransaccionId(Long transaccionId) {
        this.transaccionId = transaccionId;
    }

    public String getTransaccionNumber() {
        return transaccionNumber;
    }

    public void setTransaccionNumber(String transaccionNumber) {
        this.transaccionNumber = transaccionNumber;
    }

    public Timestamp getTransaccionDate() {
        return transaccionDate;
    }

    public void setTransaccionDate(Timestamp transaccionDate) {
        this.transaccionDate = transaccionDate;
    }

    public Double getTransaccionMonto() {
        return transaccionMonto;
    }

    public void setTransaccionMonto(Double transaccionMonto) {
        this.transaccionMonto = transaccionMonto;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransaccionDetalle() {
        return transaccionDetalle;
    }

    public void setTransaccionDetalle(String transaccionDetalle) {
        this.transaccionDetalle = transaccionDetalle;
    }

    public String getTransaccionGlossa() {
        return transaccionGlossa;
    }

    public void setTransaccionGlossa(String transaccionGlossa) {
        this.transaccionGlossa = transaccionGlossa;
    }

    public AutorizacionModel getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(AutorizacionModel autorizacion) {
        this.autorizacion = autorizacion;
    }

    public String getAutorizacionNumber() {
        return autorizacionNumber;
    }

    public void setAutorizacionNumber(String autorizacionNumber) {
        this.autorizacionNumber = autorizacionNumber;
    }
}
