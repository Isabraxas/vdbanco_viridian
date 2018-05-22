package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.JuridicasModel;
import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.JuridicasRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service    
public class JuridicasServiceImpl implements JuridicasService {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JuridicasServiceImpl.class);
    
    private JuridicasRepository juridicasRepository;
    private PersonaService personaService;
    
    @Autowired
    public JuridicasServiceImpl(JuridicasRepository juridicasRepository, PersonaService personaService) {
        this.juridicasRepository = juridicasRepository;
        this.personaService = personaService;
    }

    @Override
    public Optional<JuridicasModel> getById(Long id) {
        Optional<JuridicasModel> juridicas = this.juridicasRepository.findById(id);
        if(!juridicas.isPresent()) {
            String errorMsg = "El juridicas con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.juridicasRepository.findById(id);
    }

    @Override
    public JuridicasModel getByJuridicasNumber(String number) {
        JuridicasModel juridicas = this.juridicasRepository.findByJuridicasNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(juridicas == null) {
            String errorMsg = "El juridicas con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return juridicas;
    }

    @Override
    public Page<JuridicasModel> getAll(Pageable pageable) {
        return this.juridicasRepository.findAllByOrderByJuridicasId(pageable);
    }

    

    @Override
    public JuridicasModel save(JuridicasModel juridicas) {
        log.info("Revisando si exite el juridicas por number");
        JuridicasModel juridicasModel = this.juridicasRepository.findByJuridicasNumber(juridicas.getJuridicasNumber());
        if(juridicasModel == null) {
            log.info("Creando juridicas");
            PersonaModel persona = this.personaService.getByPersonaNumber(juridicas.getJuridicasRepresentanteLegalNumber());
            if(persona != null) {
                juridicas.setJuridicasRepresentanteLegal(persona.getPersonaNombre());
                juridicas.setJuridicasRepresentante(persona);
                log.info("Almacenando  juridicas");
                this.juridicasRepository.save(juridicas);
            }
        }else{
            log.error("La persona juridica con number: "+ juridicas.getJuridicasNumber() +" ya existe");
            String errorMsg = "La persona juridica con number: "+ juridicas.getJuridicasNumber() +" ya existe";
            throw new ConflictsException(errorMsg, new ErrorDetalle(juridicas.getJuridicasId(),"409","La persona juridica con number: "+ juridicas.getJuridicasNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
        }
        return this.getByJuridicasNumber(juridicas.getJuridicasNumber());
    }

    @Override
    public JuridicasModel update(JuridicasModel juridicas) {

        log.info("Revisando si exite el juridicas por number");
        JuridicasModel currentJuridicas = this.getByJuridicasNumber(juridicas.getJuridicasNumber());
        if(currentJuridicas != null) {
            log.info("Actualizando juridicas");
            //juridicas = this.actualizarEntityJuridicas(currentJuridicas , juridicas);
            PersonaModel persona = this.personaService.getByPersonaNumber(juridicas.getJuridicasRepresentanteLegalNumber());
            if(persona != null) {
                juridicas.setJuridicasId(currentJuridicas.getJuridicasId());
                juridicas.setJuridicasRepresentanteLegal(persona.getPersonaNombre());
                juridicas.setJuridicasRepresentante(persona);
                log.info("Almacenando cambios");
                this.juridicasRepository.save(juridicas);
                return this.getByJuridicasNumber(juridicas.getJuridicasNumber());
            }
        }
        return null;
    }

    @Override
    public JuridicasModel getByRazonSocial(String razonSocial) {

        JuridicasModel juridica= this.juridicasRepository.findByJuridicasRazonSocial(razonSocial);

        if (juridica == null){
            String errorMsg = "La persona juridica con razon social: "+ razonSocial +" no fue encontrada";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(000L, "001", "La persona juridica con razon social: "+ razonSocial +" no fue encontrada", "Hemos encontrado un error intentelo mas tarde"));

        }
        return juridica;
    }


    @Override
    public void delete(JuridicasModel juridicas) {
        //this.juridicasRepository.deleteById(juridicas.getJuridicasId());
        this.juridicasRepository.deleteAllByJuridicasNumber(juridicas.getJuridicasNumber());
    }
}
