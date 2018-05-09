package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JuridicasModelList extends ResourceSupport {

    private List<JuridicasModel> juridicasModelList;
    private String nextPage;

    public JuridicasModelList(List<JuridicasModel> juridicasModelList) {
        this.juridicasModelList = juridicasModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<JuridicasModel> getJuridicasModelList() {
        return juridicasModelList;
    }

    public void setJuridicasModelList(List<JuridicasModel> juridicasModelList) {
        this.juridicasModelList = juridicasModelList;
    }
}
