package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.AccountModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.controller.MovimientosController;
import com.vdbanco.viridianDummy.services.AccountService;
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
@RequestMapping(value = "/api/accounts")
@Api(
        name = "Cuentas",
        description = "Permite manejar a travez de una lista de metodos las cuentas del banco.",
        stage = ApiStage.ALPHA
)
public class AccountController {

    private AccountService accountService;
    private Environment env;

    @Autowired
    public AccountController(AccountService accountService, Environment env) {
        this.accountService = accountService;
        this.env = env;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiMethod(description = "Retorna una lista de cuentas paginadas de 20 en 20")
    public AccountModelList getAllPageable(@RequestParam( required = false, defaultValue = "0") int page,
                                           @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<AccountModel> accountPages = this.accountService.getAll(pageRequest);
        List<AccountModel> accountList = accountPages.getContent();

        for (AccountModel account: accountList ) {
            account.add(linkTo(methodOn(AccountController.class).getAccountByNumber(account.getAccountNumber())).withSelfRel());
        }

        AccountModelList accountModelList = new AccountModelList(accountList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(AccountController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(AccountController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        accountModelList.add(linkNext);
        accountModelList.add(linkPrevious);
        return accountModelList;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Optional<AccountModel> getAccountById(@ApiPathParam(name="id",description = "id de cuenta")@PathVariable Long id){
        return this.accountService.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/number/{number}")
    @ApiMethod(description = "Retorna la cuenta correspondiente con el numero/codigo del proporcionado en el path.")
    public AccountModel getAccountByNumber(@ApiPathParam(name="number",description = "numero/codigo de cuenta") @PathVariable String number){

        AccountModel account = this.accountService.getByAccountNumber(number);
        account.add(linkTo(methodOn(AccountController.class).getAccountByNumber(account.getAccountNumber())).withSelfRel());
        account.add(linkTo(methodOn(AccountHolderController.class).getAccountHolderByNumber(account.getAccountHolderNumber())).withRel("account holder"));
        account.add(linkTo(methodOn(ProductosBancariosController.class).getProductosBancariosByNumber(account.getProductosBancariosNumber())).withRel("producto bancario"));
        account.add(linkTo(methodOn(MovimientosController.class).getMovimientosByAccountNumber(account.getAccountNumber())).withRel("account movimientos"));
        account.add(linkTo(methodOn(MovimientosController.class).getMovimientosByAccountNumberAndLastMonths(account.getAccountNumber(),1)).withRel("account movimientos mes pasado"));
        account.add(linkTo(methodOn(AccountController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de productos bancarios"));
        return account;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiMethod(description = "Retorna la cuenta correspondiente con id proporcionado en el path.")
    public AccountModel saveAccount(@RequestBody @Valid AccountModel account){
        return this.accountService.save(account);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ApiMethod(description = "Retorna la cuenta correspondiente con el numero/codigo proporcionado en el path.")
    public AccountModel updateAccount(@RequestBody @Valid AccountModel account){

        return this.accountService.update(account);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiMethod(description = "Elimina la cuenta que corresponda con el mismo numero/codigo")
    public void deleteAccount(@RequestBody AccountModel account){
        this.accountService.delete(account);
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
