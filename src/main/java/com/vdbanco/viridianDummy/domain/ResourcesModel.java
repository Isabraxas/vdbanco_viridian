package com.vdbanco.viridianDummy.domain;

import org.springframework.hateoas.ResourceSupport;

public class ResourcesModel extends ResourceSupport{
    private String users;
    private String empleados;
    private String personas;
    private String juridicas;
    private String accounts;
    private String productosBancarios;
    private String accountHolders;
    private String autorizaciones;
    private String transacciones;


    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getEmpleados() {
        return empleados;
    }

    public void setEmpleados(String empleados) {
        this.empleados = empleados;
    }

    public String getPersonas() {
        return personas;
    }

    public void setPersonas(String personas) {
        this.personas = personas;
    }

    public String getJuridicas() {
        return juridicas;
    }

    public void setJuridicas(String juridicas) {
        this.juridicas = juridicas;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getProductosBancarios() {
        return productosBancarios;
    }

    public void setProductosBancarios(String productosBancarios) {
        this.productosBancarios = productosBancarios;
    }

    public String getAccountHolders() {
        return accountHolders;
    }

    public void setAccountHolders(String accountHolders) {
        this.accountHolders = accountHolders;
    }

    public String getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(String autorizaciones) {
        this.autorizaciones = autorizaciones;
    }

    public String getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(String transacciones) {
        this.transacciones = transacciones;
    }
}
