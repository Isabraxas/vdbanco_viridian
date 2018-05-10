package com.vdbanco.viridianDummy.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "PRODUCTOS_BANCARIOS")
@Entity
public class ProductosBancariosModel extends ResourceSupport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTO_BANCARIO_SEQ")
    @SequenceGenerator(sequenceName = "PRODUCTO_BANCARIO_ID_SEQ", allocationSize = 1, name = "PRODUCTO_BANCARIO_SEQ")
    @Column(name = "PRODUCTOS_BANCARIO_ID")
    private Long productosBancariosId;

    @NotNull
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
