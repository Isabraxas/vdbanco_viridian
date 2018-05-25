package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.AccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountServiceImpl.class);

    private AccountRepository accountRepository;
    private AccountHolderService accountHolderService;
    private ProductosBancariosService productosBancariosService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountHolderService accountHolderService, ProductosBancariosService productosBancariosService) {
        this.accountRepository = accountRepository;
        this.accountHolderService = accountHolderService;
        this.productosBancariosService = productosBancariosService;
    }

    @Override
    public Optional<AccountModel> getById(Long id) {
        Optional<AccountModel> account = this.accountRepository.findById(id);
        if(!account.isPresent()) {
            String errorMsg = "El account con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "404", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.accountRepository.findById(id);
    }

    @Override
    public AccountModel getByAccountNumber(String number) {
        AccountModel account = this.accountRepository.findByAccountNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(account == null) {
            String errorMsg = "El account con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "404", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return account;
    }

    @Override
    public AccountModel getByAccountNumberAndProductosBancarios(String number, List<String> numberProductos) {

        AccountModel account = this.accountRepository.findByAccountNumberAndProductosBancariosNumberIn(number, numberProductos);
        Long id= Long.valueOf(number.substring(4));
        if(account == null) {
            String errorMsg = "La cuenta con Id: "+ id +" no fue encontrada asociada a este tipo de producto bancario";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "404", "no se encontro en la BD esta cuenta asociada a este tipo de producto bancario", "Hemos encontrado un error intentelo mas tarde"));
        }
        return account;
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
    public AccountModel save(AccountModel account) {
        log.info("Revisando si exite el account por number");
        AccountModel accountModel = this.accountRepository.findByAccountNumber(account.getAccountNumber());
        if(accountModel == null) {
            log.info("Creando account");
            AccountHolderModel accountHolder = this.accountHolderService.getByAccountHolderNumber(account.getAccountHolderNumber());
            ProductosBancariosModel productosBancarios = this.productosBancariosService.getByProductosBancariosNumber(account.getProductosBancariosNumber());

            if(accountHolder != null && productosBancarios != null) {

                account.setAccountHolder(accountHolder);
                account.setProductoBancario(productosBancarios);
                log.info("Almacenando  account");
                this.accountRepository.save(account);
            }
        }else{
            log.error("El account con number: "+ account.getAccountNumber() +" ya existe");
            String errorMsg = "El account con number: "+ account.getAccountNumber() +" ya existe";
            throw new ConflictsException(errorMsg, new ErrorDetalle(account.getAccountId(),"409","El account con number: "+ account.getAccountNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
        }
        return this.getByAccountNumber(account.getAccountNumber());
    }


    @Override
    public AccountModel update(AccountModel account) {

        log.info("Revisando si exite el account por number");
        AccountModel currentAccount = this.getByAccountNumber(account.getAccountNumber());
        if(currentAccount != null) {
            log.info("Actualizando account");
            //account = this.actualizarEntityAccount(currentAccount , account);
            AccountHolderModel accountHolder = this.accountHolderService.getByAccountHolderNumber(account.getAccountHolderNumber());
            ProductosBancariosModel productosBancarios = this.productosBancariosService.getByProductosBancariosNumber(account.getProductosBancariosNumber());

            if(accountHolder != null && productosBancarios != null) {

                account.setAccountId(currentAccount.getAccountId());
                account.setAccountHolder(accountHolder);
                account.setProductoBancario(productosBancarios);
                log.info("Almacenando cambios");
                this.accountRepository.save(account);
                return this.getByAccountNumber(account.getAccountNumber());
            }
        }
        return null;
    }


    @Override
    public void delete(AccountModel account) {
        //this.accountRepository.deleteById(account.getAccountId());
        this.accountRepository.deleteByAccountNumber(account.getAccountNumber());
    }
    
}
