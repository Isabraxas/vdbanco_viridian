package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.PersonaRepository;
import com.vdbanco.viridianDummy.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/personas")
public class PersonaController {
    
    private PersonaService personaService;
    
    @Autowired
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public Page<PersonaModel> getAllPageable(Pageable pageable){
        return this.personaService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<PersonaModel> getPersonaById(@PathVariable Long id){
        return this.personaService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public PersonaModel getPersonaByNumber(@PathVariable String number){
        return this.personaService.getByPersonaNumber(number);
    }

    @PostMapping
    public PersonaModel savePersona(@RequestBody PersonaModel persona){
        return this.personaService.save(persona);
    }

    @PutMapping
    public PersonaModel updatePersona(@RequestBody PersonaModel persona){
        return this.personaService.update(persona);
    }

    @DeleteMapping
    public void deletePersona(@RequestBody PersonaModel persona){
        this.personaService.delete(persona);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoEncontradoRestException.class)
    public EntidadError handleNotFound(NoEncontradoRestException exception){
        EntidadError error = new EntidadError();
        error.setId(exception.getErrorNoEncontrado().getId());
        error.setEstado("error");
        error.setError(exception.getErrorNoEncontrado());
        return error;
    }
}
