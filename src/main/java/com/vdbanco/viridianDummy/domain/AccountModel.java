package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Table(name = "ACCOUNT")
@Entity
@JsonIgnoreProperties("")
public class AccountModel extends ResourceSupport implements Serializable {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_HOLDER_ID")
    private AccountHolderModel accountHolder;
    private String accountHolderNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCTOS_BANCARIOS_ID")
    private ProductosBancariosModel productoBancario;
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

    public AccountHolderModel getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(AccountHolderModel accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getAccountHolderNumber() {
        return accountHolderNumber;
    }

    public void setAccountHolderNumber(String accountHolderNumber) {
        this.accountHolderNumber = accountHolderNumber;
    }

    public ProductosBancariosModel getProductoBancario() {
        return productoBancario;
    }

    public void setProductoBancario(ProductosBancariosModel productoBancario) {
        this.productoBancario = productoBancario;
    }

    public String getProductosBancariosNumber() {
        return productosBancariosNumber;
    }

    public void setProductosBancariosNumber(String productosBancariosNumber) {
        this.productosBancariosNumber = productosBancariosNumber;
    }
}
