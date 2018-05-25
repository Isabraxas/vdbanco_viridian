package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.domain.JuridicasModel;
import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.AccountHolderRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountHolderServiceImpl.class);
    
    private AccountHolderRepository accountHolderRepository;
    private PersonaService personaService;
    private JuridicasService juridicasService;

    @Autowired
    public AccountHolderServiceImpl(AccountHolderRepository accountHolderRepository, PersonaService personaService, JuridicasService juridicasService) {
        this.accountHolderRepository = accountHolderRepository;
        this.personaService = personaService;
        this.juridicasService = juridicasService;
    }

    @Override
    public Optional<AccountHolderModel> getById(Long id) {
        Optional<AccountHolderModel> accountHolder = this.accountHolderRepository.findById(id);
        if(!accountHolder.isPresent()) {
            String errorMsg = "El accountHolder con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "404", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.accountHolderRepository.findById(id);
    }

    @Override
    public AccountHolderModel getByAccountHolderNumber(String number) {
        AccountHolderModel accountHolder = this.accountHolderRepository.findByAccountHolderNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(accountHolder == null) {
            String errorMsg = "El accountHolder con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "404", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return accountHolder;
    }

    @Override
    public AccountHolderModel getAccountHolderByPersonaNumber(String number) {
        AccountHolderModel accountHolder = this.accountHolderRepository.findAllByPersonaPersonaNumber(number);
        if (accountHolder == null){
            String errorMsg = "El accountHolder con PersonaNumber: "+ number +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(0000L, "404", "El accountHolder con PersonaNumber: "+ number +" no fue encontrado", "Hemos encontrado un error intentelo mas tarde"));
        }else {
            return accountHolder;
        }

    }

    @Override
    public AccountHolderModel getAccountHolderByJuridicasNumber(String number) {
        AccountHolderModel accountHolder = this.accountHolderRepository.findByJuridicasJuridicasNumber(number);
        if (accountHolder == null){
            String errorMsg = "El accountHolder con JuridicasNumber: "+ number +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(0000L, "404", "El accountHolder con JuridicasNumber: "+ number +" no fue encontrado", "Hemos encontrado un error intentelo mas tarde"));
        }
        return accountHolder;
    }

    @Override
    public AccountHolderModel getAccountHolderByPersonaNumberOrTitularOrApoderado(String number) {
        return this.accountHolderRepository.findAllByPersonaPersonaNumberOrAccountHolderTitularNumberOrAccountHolderApoderado(number, number, number);
    }

    @Override
    public Page<AccountHolderModel> getAll(Pageable pageable) {
        return this.accountHolderRepository.findAllByOrderByAccountHolderId(pageable);
    }

    @Override
    public AccountHolderModel save(AccountHolderModel accountHolder) {
        log.info("Revisando si exite el accountHolder por number");
        AccountHolderModel accountHolderModel = this.accountHolderRepository.findByAccountHolderNumber(accountHolder.getAccountHolderNumber());
        if(accountHolderModel == null) {

            log.info("Creando accountHolder");
            PersonaModel persona = new PersonaModel();
            if(accountHolder.getPersonaPersonaNumber() != null) {
                persona = this.personaService.getByPersonaNumber(accountHolder.getPersonaPersonaNumber());
                accountHolder.setPersona(persona);
            }
            JuridicasModel juridicas = new JuridicasModel();
            if(accountHolder.getJuridicasJuridicasNumber() != null) {
                juridicas = this.juridicasService.getByJuridicasNumber(accountHolder.getJuridicasJuridicasNumber());
                accountHolder.setJuridica(juridicas);
            }

            log.info("Almacenando  accountHolder");
            this.accountHolderRepository.save(accountHolder);

        }else{
            log.error("El accountHolder con number: "+ accountHolder.getAccountHolderNumber() +" ya existe");
            String errorMsg = "El accountHolder con number: "+ accountHolder.getAccountHolderNumber() +" ya existe";
            throw new ConflictsException(errorMsg, new ErrorDetalle(accountHolder.getAccountHolderId(),"409","El accountHolder con number: "+ accountHolder.getAccountHolderNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
        }
        return this.getByAccountHolderNumber(accountHolder.getAccountHolderNumber());
    }

    @Override
    public AccountHolderModel update(AccountHolderModel accountHolder) {

        log.info("Revisando si exite el accountHolder por number");
        AccountHolderModel currentAccountHolder = this.getByAccountHolderNumber(accountHolder.getAccountHolderNumber());
        if(currentAccountHolder != null) {
            log.info("Actualizando accountHolder");
            //accountHolder = this.actualizarEntityAccountHolder(currentAccountHolder , accountHolder);
            PersonaModel persona = new PersonaModel();
            if(accountHolder.getPersonaPersonaNumber() != null) {
                persona = this.personaService.getByPersonaNumber(accountHolder.getPersonaPersonaNumber());
                accountHolder.setPersona(persona);
            }
            JuridicasModel juridicas = new JuridicasModel();
            if(accountHolder.getJuridicasJuridicasNumber() != null) {
                juridicas = this.juridicasService.getByJuridicasNumber(accountHolder.getJuridicasJuridicasNumber());
                accountHolder.setJuridica(juridicas);
            }

            accountHolder.setAccountHolderId(currentAccountHolder.getAccountHolderId());
            log.info("Almacenando cambios");
            this.accountHolderRepository.save(accountHolder);
            return this.getByAccountHolderNumber(accountHolder.getAccountHolderNumber());

        }
        return null;
    }


    @Override
    public void delete(AccountHolderModel accountHolder) {
        //this.accountHolderRepository.deleteById(accountHolder.getAccountHolderId());
        this.accountHolderRepository.deleteByAccountHolderNumber(accountHolder.getAccountHolderNumber());
    }
}
