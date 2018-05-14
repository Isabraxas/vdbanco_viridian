package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.PersonaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


public interface PersonaRepository extends JpaRepository<PersonaModel, Long>, PagingAndSortingRepository<PersonaModel, Long> {

    PersonaModel findByPersonaNumber(String number);

    Page<PersonaModel> findAllByOrderByPersonaId(Pageable pageable);

    @Transactional
    void deleteByPersonaNumberIn(String personaNumber);
}
