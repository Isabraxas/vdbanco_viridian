package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.AutorizacionRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutorizacionServiceImpl implements AutorizacionService{

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AutorizacionServiceImpl.class);


    private AutorizacionRepository autorizacionRepository;

    @Autowired
    public AutorizacionServiceImpl(AutorizacionRepository autorizacionRepository) {
        this.autorizacionRepository = autorizacionRepository;
    }

    @Override
    public Optional<AutorizacionModel> getById(Long id) {
        Optional<AutorizacionModel> autorizacion = this.autorizacionRepository.findById(id);
        if(!autorizacion.isPresent()) {
            String errorMsg = "El autorizacion con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.autorizacionRepository.findById(id);
    }

    @Override
    public AutorizacionModel getByAutorizacionNumber(String number) {
        AutorizacionModel autorizacion = this.autorizacionRepository.findByAutorizacionNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(autorizacion == null) {
            String errorMsg = "La autorizacion con Id: "+ id +" no fue encontrada";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001",  "La autorizacion con number: "+ number +" no fue encontrada", "Hemos encontrado un error intentelo mas tarde"));
        }
        return autorizacion;
    }

    @Override
    public Page<AutorizacionModel> getAll(Pageable pageable) {
        return this.autorizacionRepository.findAllByOrderByAutorizacionId(pageable);
    }

    @Override
    public AutorizacionModel save(AutorizacionModel autorizacion) {
        log.info("Revisando si exite el autorizacion por number");
        AutorizacionModel autorizacionModel = this.autorizacionRepository.findByAutorizacionNumber(autorizacion.getAutorizacionNumber());
        if(autorizacionModel == null) {
            log.info("Creando autorizacion");
                log.info("Almacenando  autorizacion");
                this.autorizacionRepository.save(autorizacion);

        }else{
            log.error("El autorizacion con number: "+ autorizacion.getAutorizacionNumber() +" ya existe");
            String errorMsg = "El autorizacion con number: "+ autorizacion.getAutorizacionNumber() +" ya existe";
            throw new ConflictsException(errorMsg, new ErrorDetalle(autorizacion.getAutorizacionId(),"409","El autorizacion con number: "+ autorizacion.getAutorizacionNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
        }
        return this.getByAutorizacionNumber(autorizacion.getAutorizacionNumber());
    }
    
    @Override
    public AutorizacionModel update(AutorizacionModel autorizacion) {

        log.info("Revisando si exite el autorizacion por number");
        AutorizacionModel currentAutorizacion = this.getByAutorizacionNumber(autorizacion.getAutorizacionNumber());
        if(currentAutorizacion != null) {
            log.info("Actualizando autorizacion");
            //autorizacion = this.actualizarEntityAutorizacion(currentAutorizacion , autorizacion);
                autorizacion.setAutorizacionId(currentAutorizacion.getAutorizacionId());
                log.info("Almacenando cambios");
                this.autorizacionRepository.save(autorizacion);
                return this.getByAutorizacionNumber(autorizacion.getAutorizacionNumber());
           
        }
        return null;
    }
    

    @Override
    public void delete(AutorizacionModel autorizacion) {
        //this.autorizacionRepository.deleteById(autorizacion.getAutorizacionId());
        this.autorizacionRepository.deleteByAutorizacionNumber(autorizacion.getAutorizacionNumber());
    }
}
