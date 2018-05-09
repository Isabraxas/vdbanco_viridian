package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountModelList extends ResourceSupport {

    private List<AccountModel> accountModelList;
    private String nextPage;

    public AccountModelList(List<AccountModel> accountModelList) {
        this.accountModelList = accountModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<AccountModel> getAccountModelList() {
        return accountModelList;
    }

    public void setAccountModelList(List<AccountModel> accountModelList) {
        this.accountModelList = accountModelList;
    }
}
