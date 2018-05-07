package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name="AUTORIZACION")
@Entity
public class AutorizacionModel implements Serializable {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_SEQ")
    //@SequenceGenerator(sequenceName = "AUTORIZACION_ID_SEQ", allocationSize = 1, name = "AUTH_SEQ")
    private Long autorizacionId;
    private String autorizacionNumber;
    private String empleadoNumber;
    private String empleadoNumberAuth1;
    private String empleadoNumberAuth2;
    private String debitoAccountHolderNumber;
    private String debitoAccountNumber;
    private String creditoAccountHolderNumber;
    private String autorizacionType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp autorizacionDateInicio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp autorizacionDateFin;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp autorizacionDateAuth1;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp autorizacionDateAuth2;
    private String autorizacionDetalle;
    private String autorizacionGlossa;


    public Long getAutorizacionId() {
        return autorizacionId;
    }

    public void setAutorizacionId(Long autorizacionId) {
        this.autorizacionId = autorizacionId;
    }

    public String getAutorizacionNumber() {
        return autorizacionNumber;
    }

    public void setAutorizacionNumber(String autorizacionNumber) {
        this.autorizacionNumber = autorizacionNumber;
    }

    public String getEmpleadoNumber() {
        return empleadoNumber;
    }

    public void setEmpleadoNumber(String empleadoNumber) {
        this.empleadoNumber = empleadoNumber;
    }

    public String getEmpleadoNumberAuth1() {
        return empleadoNumberAuth1;
    }

    public void setEmpleadoNumberAuth1(String empleadoNumberAuth1) {
        this.empleadoNumberAuth1 = empleadoNumberAuth1;
    }

    public String getEmpleadoNumberAuth2() {
        return empleadoNumberAuth2;
    }

    public void setEmpleadoNumberAuth2(String empleadoNumberAuth2) {
        this.empleadoNumberAuth2 = empleadoNumberAuth2;
    }

    public String getDebitoAccountHolderNumber() {
        return debitoAccountHolderNumber;
    }

    public void setDebitoAccountHolderNumber(String debitoAccountHolderNumber) {
        this.debitoAccountHolderNumber = debitoAccountHolderNumber;
    }

    public String getDebitoAccountNumber() {
        return debitoAccountNumber;
    }

    public void setDebitoAccountNumber(String debitoAccountNumber) {
        this.debitoAccountNumber = debitoAccountNumber;
    }

    public String getCreditoAccountHolderNumber() {
        return creditoAccountHolderNumber;
    }

    public void setCreditoAccountHolderNumber(String creditoAccountHolderNumber) {
        this.creditoAccountHolderNumber = creditoAccountHolderNumber;
    }

    public String getAutorizacionType() {
        return autorizacionType;
    }

    public void setAutorizacionType(String autorizacionType) {
        this.autorizacionType = autorizacionType;
    }

    public Timestamp getAutorizacionDateInicio() {
        return autorizacionDateInicio;
    }

    public void setAutorizacionDateInicio(Timestamp autorizacionDateInicio) {
        this.autorizacionDateInicio = autorizacionDateInicio;
    }

    public Timestamp getAutorizacionDateFin() {
        return autorizacionDateFin;
    }

    public void setAutorizacionDateFin(Timestamp autorizacionDateFin) {
        this.autorizacionDateFin = autorizacionDateFin;
    }

    public Timestamp getAutorizacionDateAuth1() {
        return autorizacionDateAuth1;
    }

    public void setAutorizacionDateAuth1(Timestamp autorizacionDateAuth1) {
        this.autorizacionDateAuth1 = autorizacionDateAuth1;
    }

    public Timestamp getAutorizacionDateAuth2() {
        return autorizacionDateAuth2;
    }

    public void setAutorizacionDateAuth2(Timestamp autorizacionDateAuth2) {
        this.autorizacionDateAuth2 = autorizacionDateAuth2;
    }

    public String getAutorizacionDetalle() {
        return autorizacionDetalle;
    }

    public void setAutorizacionDetalle(String autorizacionDetalle) {
        this.autorizacionDetalle = autorizacionDetalle;
    }

    public String getAutorizacionGlossa() {
        return autorizacionGlossa;
    }

    public void setAutorizacionGlossa(String autorizacionGlossa) {
        this.autorizacionGlossa = autorizacionGlossa;
    }
}
