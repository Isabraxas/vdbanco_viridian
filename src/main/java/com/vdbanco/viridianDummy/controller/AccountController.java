package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

}
