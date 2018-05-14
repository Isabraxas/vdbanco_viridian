package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.domain.AccountHolderModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.AccountHolderService;
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
@RequestMapping(value = "/api/accountHolders")
public class AccountHolderController {

    private AccountHolderService accountHolderService;
    private Environment env;

    @Autowired
    public AccountHolderController(AccountHolderService accountHolderService, Environment env) {
        this.accountHolderService = accountHolderService;
        this.env = env;
    }

    @GetMapping
    public AccountHolderModelList getAllPageable(@RequestParam( required = false, defaultValue = "0") int page,
                                           @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<AccountHolderModel> accountHolderPages = this.accountHolderService.getAll(pageRequest);
        List<AccountHolderModel> accountHolderList = accountHolderPages.getContent();

        for (AccountHolderModel accountHolder: accountHolderList ) {
            accountHolder.add(linkTo(methodOn(AccountHolderController.class).getAccountHolderByNumber(accountHolder.getAccountHolderNumber())).withSelfRel());
        }

        AccountHolderModelList accountHolderModelList = new AccountHolderModelList(accountHolderList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(AccountHolderController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(AccountHolderController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        accountHolderModelList.add(linkNext);
        accountHolderModelList.add(linkPrevious);
        return accountHolderModelList;

    }

    @GetMapping(value = "/{id}")
    public Optional<AccountHolderModel> getAccountHolderById(@PathVariable Long id){
        return this.accountHolderService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public AccountHolderModel getAccountHolderByNumber(@PathVariable String number){

        AccountHolderModel accountHolder = this.accountHolderService.getByAccountHolderNumber(number);
        accountHolder.add(linkTo(methodOn(AccountHolderController.class).getAccountHolderByNumber(accountHolder.getAccountHolderNumber())).withSelfRel());
        if(accountHolder.getPersonaPersonaNumber() != null)
        accountHolder.add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(accountHolder.getPersonaPersonaNumber())).withRel("persona"));
        if(accountHolder.getJuridicasJuridicasNumber() != null)
        accountHolder.add(linkTo(methodOn(JuridicasController.class).getJuridicasByNumber(accountHolder.getJuridicasJuridicasNumber())).withRel("juridica"));
        accountHolder.add(linkTo(methodOn(AccountHolderController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de accountHolders"));
        return accountHolder;
    }

    @PostMapping
    public AccountHolderModel saveAccountHolder(@RequestBody @Valid AccountHolderModel accountHolder){
        return this.accountHolderService.save(accountHolder);
    }

    @PutMapping
    public AccountHolderModel updateAccountHolder(@RequestBody @Valid AccountHolderModel accountHolder){

        return this.accountHolderService.update(accountHolder);
    }

    @DeleteMapping
    public void deleteAccountHolder(@RequestBody AccountHolderModel accountHolder){
        this.accountHolderService.delete(accountHolder);
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
