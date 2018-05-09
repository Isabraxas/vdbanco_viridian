package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonaModelList extends ResourceSupport {

    private List<PersonaModel> personaModelList;
    private String nextPage;

    public PersonaModelList(List<PersonaModel> personaModelList) {
        this.personaModelList = personaModelList;
    }


    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<PersonaModel> getPersonaModelList() {
        return personaModelList;
    }

    public void setPersonaModelList(List<PersonaModel> personaModelList) {
        this.personaModelList = personaModelList;
    }
}
