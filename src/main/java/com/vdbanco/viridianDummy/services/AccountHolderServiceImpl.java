package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.AccountHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    private AccountHolderRepository accountHolderRepository;

    @Autowired
    public AccountHolderServiceImpl(AccountHolderRepository accountHolderRepository) {
        this.accountHolderRepository = accountHolderRepository;
    }

    @Override
    public Optional<AccountHolderModel> getById(Long id) {
        Optional<AccountHolderModel> accountHolder = this.accountHolderRepository.findById(id);
        if(!accountHolder.isPresent()) {
            String errorMsg = "El accountHolder con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.accountHolderRepository.findById(id);
    }

    @Override
    public AccountHolderModel getByAccountHolderNumber(String number) {
        AccountHolderModel accountHolder = this.accountHolderRepository.findByAccountHolderNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(accountHolder == null) {
            String errorMsg = "El accountHolder con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return accountHolder;
    }

    @Override
    public AccountHolderModel getAccountHolderByPersonaNumber(String number) {
        return this.accountHolderRepository.findAllByPersonaPersonaNumber(number);
    }

    @Override
    public AccountHolderModel getAccountHolderByPersonaNumberOrTitularOrApoderado(String number) {
        return this.accountHolderRepository.findAllByPersonaPersonaNumberOrAccountHolderTitularNumberOrAccountHolderApoderado(number, number, number);
    }

    @Override
    public AccountHolderModel save(AccountHolderModel accountHolder) {
        boolean existe = this.accountHolderRepository.existsById(accountHolder.getAccountHolderId());
        if(!existe) {
            this.accountHolderRepository.save(accountHolder);
        }
        return this.getByAccountHolderNumber(accountHolder.getAccountHolderNumber());
    }

    @Override
    public Page<AccountHolderModel> getAll(Pageable pageable) {
        return this.accountHolderRepository.findAllByOrderByAccountHolderId(pageable);
    }

    @Override
    public AccountHolderModel update(AccountHolderModel accountHolder) {
        boolean existe = this.accountHolderRepository.existsById(accountHolder.getAccountHolderId());
        if(existe) {
            this.accountHolderRepository.save(accountHolder);
            return this.getByAccountHolderNumber(accountHolder.getAccountHolderNumber());
        }
        return null;
    }

    @Override
    public void delete(AccountHolderModel accountHolder) {
        this.accountHolderRepository.deleteById(accountHolder.getAccountHolderId());
    }
}
