package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductosBancariosModelList extends ResourceSupport {

    private List<ProductosBancariosModel> productosBancariosModelList;
    private String nextPage;

    public ProductosBancariosModelList(List<ProductosBancariosModel> productosBancariosModelList) {
        this.productosBancariosModelList = productosBancariosModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<ProductosBancariosModel> getProductosBancariosModelList() {
        return productosBancariosModelList;
    }

    public void setProductosBancariosModelList(List<ProductosBancariosModel> productosBancariosModelList) {
        this.productosBancariosModelList = productosBancariosModelList;
    }
}
