package com.vdbanco.viridianDummy.funciones;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.PersonaModel;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

public class ProductosClienteModel {

    private Long userId;
    private String estado;
    private String personaPersonaNumber;
    private PersonaModel persona;
    private List<AccountModel> cuentas;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPersonaPersonaNumber() {
        return personaPersonaNumber;
    }

    public void setPersonaPersonaNumber(String personaPersonaNumber) {
        this.personaPersonaNumber = personaPersonaNumber;
    }

    public PersonaModel getPersona() {
        return persona;
    }

    public void setPersona(PersonaModel persona) {
        this.persona = persona;
    }

    public List<AccountModel> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<AccountModel> cuentas) {
        this.cuentas = cuentas;
    }
}
