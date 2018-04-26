package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.PersonaRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonaServiceImpl implements PersonaService {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PersonaServiceImpl.class);

    // servicios

    // repositorios
    private PersonaRepository personaRepository;


    @Autowired
    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }


    @Override
    public Optional<PersonaModel> getById(Long id) {
        Optional<PersonaModel> persona = this.personaRepository.findById(id);
        if(!persona.isPresent()) {
            String errorMsg = "La persona con Id: "+ id +" no fue encontrada";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return persona;
    }

    @Override
    public PersonaModel getByPersonaNumber(String number) {
        PersonaModel persona = this.personaRepository.findByPersonaNumber(number);
        if(persona == null){
            String errorMsg = "La persona con el number: "+ number +" no fue encontrada";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(Long.valueOf(number.substring(4)), "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return persona;
    }

    @Override
    public PersonaModel save(PersonaModel persona) {
        log.info("Buscando existencia de persona");
        boolean existe = this.personaRepository.existsById(persona.getPersonaId());
        if(!existe) {
            log.info("Se puede crear la persona");
            this.personaRepository.save(persona);
            log.info("Persona creada");
        }
        return this.getByPersonaNumber(persona.getPersonaNumber());
    }

    @Override
    public Page<PersonaModel> getAll(Pageable pageable) {
        return this.personaRepository.findAllByOrderByPersonaId(pageable);
    }

    @Override
    public PersonaModel update(PersonaModel persona) {
        log.info("Buscando existencia de persona");
        boolean existe = this.personaRepository.existsById(persona.getPersonaId());
        if(existe) {
            log.info("Actualizando persona");
            this.personaRepository.save(persona);
            log.info("Persona actualizada");

            return this.getByPersonaNumber(persona.getPersonaNumber());
        }
        return null;
    }

    @Override
    public void delete(PersonaModel persona) {

        this.getById(persona.getPersonaId());
        this.personaRepository.deleteById(persona.getPersonaId());
        log.info("Persona eliminada");
    }
}
