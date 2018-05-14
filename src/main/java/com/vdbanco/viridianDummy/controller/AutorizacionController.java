package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.domain.AutorizacionModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.AutorizacionService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/api/autorizacions")
@Api(
        name = "Autorizaciones",
        description = "Permite manejar por medio de una lista de metodos las operaciones aplicadas a las autorizaciones bancarias.",
        stage = ApiStage.ALPHA
)
public class AutorizacionController {

    private AutorizacionService autorizacionService;
    private Environment env;

    @Autowired
    public AutorizacionController(AutorizacionService autorizacionService, Environment env) {
        this.autorizacionService = autorizacionService;
        this.env = env;
    }

    @GetMapping
    @ApiMethod(description = "Retorna una lista de autorizacines paginadas de 20 en 20")
    public AutorizacionModelList getAllPageable(@ApiQueryParam(name = "page", description = "numero de pagina", required = false, defaultvalue = "0")
                                                    @RequestParam( required = false, defaultValue = "0") int page,
                                                @ApiQueryParam(name = "size", description = "cantidad de elementos", required = false, defaultvalue = "20")
                                                    @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<AutorizacionModel> autorizacionPages = this.autorizacionService.getAll(pageRequest);
        List<AutorizacionModel> autorizacionList = autorizacionPages.getContent();

        for (AutorizacionModel autorizacion: autorizacionList ) {
            autorizacion.add(linkTo(methodOn(AutorizacionController.class).getAutorizacionByNumber(autorizacion.getAutorizacionNumber())).withSelfRel());
        }

        AutorizacionModelList autorizacionModelList = new AutorizacionModelList(autorizacionList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(AutorizacionController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(AutorizacionController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        autorizacionModelList.add(linkNext);
        autorizacionModelList.add(linkPrevious);
        return autorizacionModelList;

    }

    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Retorna la autorizacion correspondiente con id proporcionado en el path.")
    public Optional<AutorizacionModel> getAutorizacionById(@ApiPathParam(name="id",description = "id de autorizacion") @PathVariable Long id){
        return this.autorizacionService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    @ApiMethod(description = "Retorna la autorizacion correspondiente con el numero/codigo proporcionado en el path.")
    public AutorizacionModel getAutorizacionByNumber(@ApiPathParam(name = "number" , description = "numero o codigo de autorizacion") @PathVariable String number){

        AutorizacionModel autorizacion = this.autorizacionService.getByAutorizacionNumber(number);
        autorizacion.add(linkTo(methodOn(AutorizacionController.class).getAutorizacionByNumber(autorizacion.getAutorizacionNumber())).withSelfRel());
        autorizacion.add(linkTo(methodOn(AutorizacionController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de autorizaciones"));
        return autorizacion;
    }

    @PostMapping
    @ApiMethod(description = "Almacena una nueva autorizacion con los datos ingresados.", responsestatuscode = "201" )
    public AutorizacionModel saveAutorizacion(@RequestBody @Valid AutorizacionModel autorizacion){
        return this.autorizacionService.save(autorizacion);
    }

    @PutMapping
    @ApiMethod(description = "Sustituye los datos de la autorizacion que corresponda con el mismo numero/codigo")
    public AutorizacionModel updateAutorizacion(@RequestBody @Valid AutorizacionModel autorizacion){

        return this.autorizacionService.update(autorizacion);
    }

    @DeleteMapping
    @ApiMethod(description = "Elimina la autorizacion que corresponda con el mismo numero/codigo")
    public void deleteAutorizacion(@RequestBody AutorizacionModel autorizacion){
        this.autorizacionService.delete(autorizacion);
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
