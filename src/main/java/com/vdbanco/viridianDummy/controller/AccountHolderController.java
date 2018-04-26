package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.services.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/accountHolders")
public class AccountHolderController {

    private AccountHolderService accountHolderService;

    @Autowired
    public AccountHolderController(AccountHolderService accountHolderService) {
        this.accountHolderService = accountHolderService;
    }

    @GetMapping
    public Page<AccountHolderModel> getAllPageable(Pageable pageable){
        return this.accountHolderService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<AccountHolderModel> getAccountHolderById(@PathVariable Long id){
        return this.accountHolderService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public AccountHolderModel getAccountHolderByNumber(@PathVariable String number){
        return this.accountHolderService.getByAccountHolderNumber(number);
    }

    @GetMapping(value = "/personaNumber/{number}")
    public AccountHolderModel getAccountHolderByPersonaNumber(@PathVariable String number){
        return this.accountHolderService.getAccountHolderByPersonaNumber(number);
    }

    @PostMapping
    public AccountHolderModel saveAccountHolder(@RequestBody AccountHolderModel accountHolder){
        return this.accountHolderService.save(accountHolder);
    }

    @PutMapping
    public AccountHolderModel updateAccountHolder(@RequestBody AccountHolderModel accountHolder){

        return this.accountHolderService.update(accountHolder);
    }

    @DeleteMapping
    public void deleteAccountHolder(@RequestBody AccountHolderModel accountHolder){
        this.accountHolderService.delete(accountHolder);
    }
}
