package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.JuridicasModel;
import com.vdbanco.viridianDummy.domain.JuridicasModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.JuridicasService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
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
@RequestMapping(value = "/api/juridicas")
@Api(
        name = "Personas Juridicas",
        description = "Permite manejar a travez de una lista de metodos, las personas Juridicas del banco.",
        stage = ApiStage.ALPHA
)
public class JuridicasController {

    private JuridicasService juridicasService;
    private Environment env;

    @Autowired
    public JuridicasController(JuridicasService juridicasService, Environment env) {
        this.juridicasService = juridicasService;
        this.env = env;
    }

    @GetMapping
    @ApiMethod(description = "Retorna una lista de personas juridicas paginadas de 20 en 20")
    public JuridicasModelList getAllPageable(@RequestParam( required = false, defaultValue = "0") int page,
                                           @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<JuridicasModel> juridicasPages = this.juridicasService.getAll(pageRequest);
        List<JuridicasModel> juridicasList = juridicasPages.getContent();

        for (JuridicasModel juridicas: juridicasList ) {
            juridicas.add(linkTo(methodOn(JuridicasController.class).getJuridicasByNumber(juridicas.getJuridicasNumber())).withSelfRel());
        }

        JuridicasModelList juridicasModelList = new JuridicasModelList(juridicasList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(JuridicasController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(JuridicasController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        juridicasModelList.add(linkNext);
        juridicasModelList.add(linkPrevious);
        return juridicasModelList;

    }

    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Retorna los datos de persona juridica correspondiente con id proporcionado en el path.")
    public Optional<JuridicasModel> getJuridicasById(@ApiPathParam(name="id",description = "id de persona Juridica") @PathVariable Long id){
        return this.juridicasService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    @ApiMethod(description = "Retorna los datos de persona juridica correspondiente con el numero/codigo proporcionado en el path.")
    public JuridicasModel getJuridicasByNumber(@ApiPathParam(name = "number" , description = "numero o codigo de persona juridica") @PathVariable String number){
        
        JuridicasModel juridicas = this.juridicasService.getByJuridicasNumber(number);
        juridicas.add(linkTo(methodOn(JuridicasController.class).getJuridicasByNumber(juridicas.getJuridicasNumber())).withSelfRel());
        juridicas.add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(juridicas.getJuridicasRepresentanteLegalNumber())).withRel("representante legal"));
        juridicas.add(linkTo(methodOn(JuridicasController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de juridicas"));
        return juridicas;
    }

    @PostMapping
    @ApiMethod(description = "Almacena una nueva persona juridica con los datos ingresados.", responsestatuscode = "201" )
    public JuridicasModel saveJuridicas(@RequestBody @Valid JuridicasModel juridicas){
        return this.juridicasService.save(juridicas);
    }

    @PutMapping
    @ApiMethod(description = "Sustituye los datos de persona juridica que corresponda con el mismo numero/codigo")
    public JuridicasModel updateJuridicas(@RequestBody @Valid JuridicasModel juridicas){

        return this.juridicasService.update(juridicas);
    }

    @DeleteMapping
    @ApiMethod(description = "Elimina la persona juridica que corresponda con el mismo numero/codigo")
    public void deleteJuridicas(@RequestBody JuridicasModel juridicas){
        this.juridicasService.delete(juridicas);
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
