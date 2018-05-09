package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountHolderModelList extends ResourceSupport {

    private List<AccountHolderModel> accountHolderModelList;
    private String nextPage;

    public AccountHolderModelList(List<AccountHolderModel> accountHolderModelList) {
        this.accountHolderModelList = accountHolderModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<AccountHolderModel> getAccountHolderModelList() {
        return accountHolderModelList;
    }

    public void setAccountHolderModelList(List<AccountHolderModel> accountHolderModelList) {
        this.accountHolderModelList = accountHolderModelList;
    }
}
