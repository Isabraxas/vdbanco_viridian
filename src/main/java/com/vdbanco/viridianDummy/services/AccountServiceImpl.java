package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    
    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<AccountModel> getById(Long id) {
        Optional<AccountModel> account = this.accountRepository.findById(id);
        if(!account.isPresent()) {
            String errorMsg = "El account con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.accountRepository.findById(id);
    }

    @Override
    public AccountModel getByAccountNumber(String number) {
        AccountModel account = this.accountRepository.findByAccountNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(account == null) {
            String errorMsg = "El account con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return account;
    }


    public AccountModel getByAccountNumberAndProductosBancarios(String number, List<String>numberProductos) {

        AccountModel account = this.accountRepository.findByAccountNumberandAndProductosBancariosNumberIn(number, numberProductos);
        Long id= Long.valueOf(number.substring(4));
        if(account == null) {
            String errorMsg = "El account con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return account;
    }

    @Override
    public AccountModel save(AccountModel account) {
        boolean existe = this.accountRepository.existsById(account.getAccountId());
        if(!existe) {
            this.accountRepository.save(account);
        }
        return this.getByAccountNumber(account.getAccountNumber());
    }

    @Override
    public Page<AccountModel> getAll(Pageable pageable) {
        return this.accountRepository.findAllByOrderByAccountId(pageable);
    }

    @Override
    public List<AccountModel> getAccountByAccountHolder(String number) {
        return this.accountRepository.findByAccountHolderNumber(number);
    }

    @Override
    public AccountModel update(AccountModel account) {
        boolean existe = this.accountRepository.existsById(account.getAccountId());
        if(existe) {
            this.accountRepository.save(account);
            return this.getByAccountNumber(account.getAccountNumber());
        }
        return null;
    }

    @Override
    public void delete(AccountModel account) {
        this.accountRepository.deleteById(account.getAccountId());
    }

}
