package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransaccionModelList extends ResourceSupport {

    private List<TransaccionModel> transaccionModelList;
    private String nextPage;

    public TransaccionModelList(List<TransaccionModel> transaccionModelList) {
        this.transaccionModelList = transaccionModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<TransaccionModel> getTransaccionModelList() {
        return transaccionModelList;
    }

    public void setTransaccionModelList(List<TransaccionModel> transaccionModelList) {
        this.transaccionModelList = transaccionModelList;
    }
}
