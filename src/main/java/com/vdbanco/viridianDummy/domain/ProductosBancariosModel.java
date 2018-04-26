package com.vdbanco.viridianDummy.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "PRODUCTOS_BANCARIOS")
@Entity
public class ProductosBancariosModel implements Serializable {

    @Id
    @Column(name = "PRODUCTOS_BANCARIO_ID")
    private Long productosBancariosId;
    private String productosBancariosNumber;
    private String productosBancariosNombre;

    public Long getProductosBancariosId() {
        return productosBancariosId;
    }

    public void setProductosBancariosId(Long productosBancariosId) {
        this.productosBancariosId = productosBancariosId;
    }

    public String getProductosBancariosNumber() {
        return productosBancariosNumber;
    }

    public void setProductosBancariosNumber(String productosBancariosNumber) {
        this.productosBancariosNumber = productosBancariosNumber;
    }

    public String getProductosBancariosNombre() {
        return productosBancariosNombre;
    }

    public void setProductosBancariosNombre(String productosBancariosNombre) {
        this.productosBancariosNombre = productosBancariosNombre;
    }
}
