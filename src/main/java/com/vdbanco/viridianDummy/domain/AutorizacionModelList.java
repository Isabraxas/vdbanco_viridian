package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutorizacionModelList extends ResourceSupport {

    private List<AutorizacionModel> autorizacionModelList;
    private String nextPage;

    public AutorizacionModelList(List<AutorizacionModel> autorizacionModelList) {
        this.autorizacionModelList = autorizacionModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<AutorizacionModel> getAutorizacionModelList() {
        return autorizacionModelList;
    }

    public void setAutorizacionModelList(List<AutorizacionModel> autorizacionModelList) {
        this.autorizacionModelList = autorizacionModelList;
    }
}
