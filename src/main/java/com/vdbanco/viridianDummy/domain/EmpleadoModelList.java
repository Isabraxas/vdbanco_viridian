package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmpleadoModelList extends ResourceSupport {

    private List<EmpleadoModel> empleadoModelList;
    private String nextPage;

    public EmpleadoModelList(List<EmpleadoModel> empleadoModelList) {
        this.empleadoModelList = empleadoModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<EmpleadoModel> getEmpleadoModelList() {
        return empleadoModelList;
    }

    public void setEmpleadoModelList(List<EmpleadoModel> empleadoModelList) {
        this.empleadoModelList = empleadoModelList;
    }
}
