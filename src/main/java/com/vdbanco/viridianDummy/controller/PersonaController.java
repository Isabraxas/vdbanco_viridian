package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.domain.PersonaModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.PersonaService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/personas")
@Api(
        name = "Personas Naturales",
        description = "Permite manejar a travez de una lista de metodos los datos de personas en el banco.",
        stage = ApiStage.ALPHA
)
public class PersonaController {

    private PersonaService personaService;
    private Environment env;

    @Autowired
    public PersonaController(PersonaService personaService, Environment env) {
        this.personaService = personaService;
        this.env = env;
    }

    @GetMapping
    @ApiMethod(description = "Retorna una lista de personas paginadas de 20 en 20")
    public PersonaModelList getAllPageable(@ApiQueryParam(name = "page", description = "numero de pagina", required = false, defaultvalue = "0")
                                               @RequestParam( required = false, defaultValue = "0") int page,
                                           @ApiQueryParam(name = "size", description = "cantidad de elementos", required = false, defaultvalue = "20")
                                                @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<PersonaModel> personaPages = this.personaService.getAll(pageRequest);
        List<PersonaModel> personaList = personaPages.getContent();

        for (PersonaModel persona: personaList ) {
            persona.add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(persona.getPersonaNumber())).withSelfRel());
        }

        PersonaModelList personaModelList = new PersonaModelList(personaList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(PersonaController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(PersonaController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        personaModelList.add(linkNext);
        personaModelList.add(linkPrevious);
        return personaModelList;

    }

    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Retorna los datos de la persona correspondiente con id proporcionado en el path.")
    public Optional<PersonaModel> getPersonaById(@ApiPathParam(name="id",description = "id de persona") @PathVariable Long id){
        return this.personaService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    @ApiMethod(description = "Retorna los datos de la persona correspondiente con el numero/codigo proporcionado en el path.")
    public PersonaModel getPersonaByNumber(@PathVariable String number){

        PersonaModel persona = this.personaService.getByPersonaNumber(number);
        persona.add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(persona.getPersonaNumber())).withSelfRel());
        persona.add(linkTo(methodOn(PersonaController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de personas"));
        return persona;
    }

    @PostMapping
    @ApiMethod(description = "Almacena una nueva persona con los datos ingresados.", responsestatuscode = "201" )
    public PersonaModel savePersona(@RequestBody @Valid PersonaModel persona){
        return this.personaService.save(persona);
    }

    @PutMapping
    @ApiMethod(description = "Sustituye los datos del persona que corresponda con el mismo numero/codigo")
    public PersonaModel updatePersona(@RequestBody @Valid PersonaModel persona){
        return this.personaService.update(persona);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping
    @ApiMethod(description = "Elimina la persona que corresponda con el mismo numero/codigo")
    public void deletePersona(@RequestBody PersonaModel persona){
        this.personaService.delete(persona);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoEncontradoRestException.class)
    public EntidadError handleNotFound(NoEncontradoRestException exception){
        EntidadError error = new EntidadError();
        error.setId(exception.getErrorDetalle().getId());
        error.setEstado("error");
        error.setError(exception.getErrorDetalle());
        return error;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictsException.class)
    public EntidadError handleConflict(ConflictsException exception){
        EntidadError error = new EntidadError();
        error.setId(exception.getErrorDetalle().getId());
        error.setEstado("error");
        error.setError(exception.getErrorDetalle());
        return error;
    }
}
