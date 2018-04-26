package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.PersonaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PersonaService  {
    Optional<PersonaModel> getById(Long id);

    PersonaModel getByPersonaNumber(String number);

    PersonaModel save(PersonaModel persona);

    Page<PersonaModel> getAll(Pageable pageable);

    PersonaModel update(PersonaModel persona);

    void delete(PersonaModel persona);
}
