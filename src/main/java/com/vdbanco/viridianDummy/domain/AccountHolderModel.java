package com.vdbanco.viridianDummy.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "ACCOUNT_HOLDER")
@Entity
public class AccountHolderModel implements Serializable {

    @Id
    private Long accountHolderId;
    private String accountHolderNumber;
    private String accountHolderTipo;
    private String accountHolderTitular;
    private String accountHolderTitularNumber;
    private String accountHolderApoderado;
    // en la base de datos esta recortado el nombre
    @Column(name = "ACCOUNT_HOLDER_APODERADO_NUMBE")
    private String accountHolderApoderadoNumber;
    private String accountHolderFirmante;
    private String accountHolderFirmanteNumber;
    private String accountHolderBanco;
    private String accountHolderBancoNumber;
    private String personaPersonaNumber;
    private String juridicasJuridicasNumber;


    public Long getAccountHolderId() {
        return accountHolderId;
    }

    public void setAccountHolderId(Long accountHolderId) {
        this.accountHolderId = accountHolderId;
    }

    public String getAccountHolderNumber() {
        return accountHolderNumber;
    }

    public void setAccountHolderNumber(String accountHolderNumber) {
        this.accountHolderNumber = accountHolderNumber;
    }

    public String getAccountHolderTipo() {
        return accountHolderTipo;
    }

    public void setAccountHolderTipo(String accountHolderTipo) {
        this.accountHolderTipo = accountHolderTipo;
    }

    public String getAccountHolderTitular() {
        return accountHolderTitular;
    }

    public void setAccountHolderTitular(String accountHolderTitular) {
        this.accountHolderTitular = accountHolderTitular;
    }

    public String getAccountHolderTitularNumber() {
        return accountHolderTitularNumber;
    }

    public void setAccountHolderTitularNumber(String accountHolderTitularNumber) {
        this.accountHolderTitularNumber = accountHolderTitularNumber;
    }

    public String getAccountHolderApoderado() {
        return accountHolderApoderado;
    }

    public void setAccountHolderApoderado(String accountHolderApoderado) {
        this.accountHolderApoderado = accountHolderApoderado;
    }

    public String getAccountHolderApoderadoNumber() {
        return accountHolderApoderadoNumber;
    }

    public void setAccountHolderApoderadoNumber(String accountHolderApoderadoNumber) {
        this.accountHolderApoderadoNumber = accountHolderApoderadoNumber;
    }

    public String getAccountHolderFirmante() {
        return accountHolderFirmante;
    }

    public void setAccountHolderFirmante(String accountHolderFirmante) {
        this.accountHolderFirmante = accountHolderFirmante;
    }

    public String getAccountHolderFirmanteNumber() {
        return accountHolderFirmanteNumber;
    }

    public void setAccountHolderFirmanteNumber(String accountHolderFirmanteNumber) {
        this.accountHolderFirmanteNumber = accountHolderFirmanteNumber;
    }

    public String getAccountHolderBanco() {
        return accountHolderBanco;
    }

    public void setAccountHolderBanco(String accountHolderBanco) {
        this.accountHolderBanco = accountHolderBanco;
    }

    public String getAccountHolderBancoNumber() {
        return accountHolderBancoNumber;
    }

    public void setAccountHolderBancoNumber(String accountHolderBancoNumber) {
        this.accountHolderBancoNumber = accountHolderBancoNumber;
    }

    public String getPersonaPersonaNumber() {
        return personaPersonaNumber;
    }

    public void setPersonaPersonaNumber(String personaPersonaNumber) {
        this.personaPersonaNumber = personaPersonaNumber;
    }

    public String getJuridicasJuridicasNumber() {
        return juridicasJuridicasNumber;
    }

    public void setJuridicasJuridicasNumber(String juridicasJuridicasNumber) {
        this.juridicasJuridicasNumber = juridicasJuridicasNumber;
    }
}
