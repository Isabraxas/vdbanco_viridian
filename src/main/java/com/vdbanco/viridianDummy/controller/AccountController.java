package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Page<AccountModel> getAllPageable(Pageable pageable){
        return this.accountService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<AccountModel> getAccountById(@PathVariable Long id){
        return this.accountService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public AccountModel getAccountByNumber(@PathVariable String number){
        return this.accountService.getByAccountNumber(number);
    }

    @PostMapping
    public AccountModel saveAccount(@RequestBody AccountModel account){
        return this.accountService.save(account);
    }

    @PutMapping
    public AccountModel updateAccount(@RequestBody AccountModel account){

        return this.accountService.update(account);
    }

    @DeleteMapping
    public void deleteAccount(@RequestBody AccountModel account){
        this.accountService.delete(account);
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
