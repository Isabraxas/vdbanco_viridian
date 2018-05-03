package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "ACCOUNT")
@Entity
public class AccountModel implements Serializable {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCOUNT_SEQ")
    //@SequenceGenerator(sequenceName = "ACCOUNT_ID_SEQ", allocationSize = 1, name = "ACCOUNT_SEQ")
    private Long accountId;
    private String accountNumber;
    private String accountTipo;
    private String accountNaturaleza;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp accountFechaApertura;
    private Double accountBalance;
    private String accountHolderNumber;
    private String productosBancariosNumber;



    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountTipo() {
        return accountTipo;
    }

    public void setAccountTipo(String accountTipo) {
        this.accountTipo = accountTipo;
    }

    public String getAccountNaturaleza() {
        return accountNaturaleza;
    }

    public void setAccountNaturaleza(String accountNaturaleza) {
        this.accountNaturaleza = accountNaturaleza;
    }

    public Timestamp getAccountFechaApertura() {
        return accountFechaApertura;
    }

    public void setAccountFechaApertura(Timestamp accountFechaApertura) {
        this.accountFechaApertura = accountFechaApertura;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountHolderNumber() {
        return accountHolderNumber;
    }

    public void setAccountHolderNumber(String accountHolderNumber) {
        this.accountHolderNumber = accountHolderNumber;
    }

    public String getProductosBancariosNumber() {
        return productosBancariosNumber;
    }

    public void setProductosBancariosNumber(String productosBancariosNumber) {
        this.productosBancariosNumber = productosBancariosNumber;
    }
}
