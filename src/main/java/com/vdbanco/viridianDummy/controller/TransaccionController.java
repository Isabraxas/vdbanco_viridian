package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.domain.TransaccionModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.TransaccionService;
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
@RequestMapping(value = "/api/transaccions")
public class TransaccionController {

    private TransaccionService transaccionService;
    private Environment env;

    @Autowired
    public TransaccionController(TransaccionService transaccionService, Environment env) {
        this.transaccionService = transaccionService;
        this.env = env;
    }

    @GetMapping
    public TransaccionModelList getAllPageable(@RequestParam( required = false, defaultValue = "0") int page,
                                           @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<TransaccionModel> transaccionPages = this.transaccionService.getAll(pageRequest);
        List<TransaccionModel> transaccionList = transaccionPages.getContent();

        for (TransaccionModel transaccion: transaccionList ) {
            transaccion.add(linkTo(methodOn(TransaccionController.class).getTransaccionByNumber(transaccion.getTransaccionNumber())).withSelfRel());
        }

        TransaccionModelList transaccionModelList = new TransaccionModelList(transaccionList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(TransaccionController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(TransaccionController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        transaccionModelList.add(linkNext);
        transaccionModelList.add(linkPrevious);
        return transaccionModelList;

    }

    @GetMapping(value = "/{id}")
    public Optional<TransaccionModel> getTransaccionById(@PathVariable Long id){
        return this.transaccionService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public List<TransaccionModel> getTransaccionByNumber(@PathVariable String number){

        List<TransaccionModel> transaccionList = this.transaccionService.getByTransaccionNumber(number);
        for (TransaccionModel transaccion: transaccionList ) {

            transaccion.add(linkTo(methodOn(TransaccionController.class).getTransaccionByNumber(transaccion.getTransaccionNumber())).withSelfRel());
            transaccion.add(linkTo(methodOn(AutorizacionController.class).getAutorizacionByNumber(transaccion.getAutorizacionNumber())).withSelfRel());
            transaccion.add(linkTo(methodOn(TransaccionController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de transacciones"));

        }
        return transaccionList;
    }

    @PostMapping
    public TransaccionModel saveTransaccion(@RequestBody @Valid TransaccionModel transaccion){
        return this.transaccionService.save(transaccion);
    }

    @PutMapping
    public TransaccionModel updateTransaccion(@RequestBody @Valid TransaccionModel transaccion){

        return this.transaccionService.update(transaccion);
    }

    @DeleteMapping
    public void deleteTransaccion(@RequestBody TransaccionModel transaccion){
        this.transaccionService.delete(transaccion);
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
