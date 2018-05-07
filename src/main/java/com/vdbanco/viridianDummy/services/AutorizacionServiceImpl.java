package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.AutorizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutorizacionServiceImpl implements AutorizacionService{
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
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.autorizacionRepository.findById(id);
    }

    @Override
    public AutorizacionModel getByAutorizacionNumber(String number) {
        AutorizacionModel autorizacion = this.autorizacionRepository.findByAutorizacionNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(autorizacion == null) {
            String errorMsg = "El autorizacion con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return autorizacion;
    }

    @Override
    public AutorizacionModel save(AutorizacionModel autorizacion) {
        boolean existe = this.autorizacionRepository.existsById(autorizacion.getAutorizacionId());
        autorizacion.setAutorizacionNumber("AU00"+autorizacion.getAutorizacionId().toString());
        if(!existe) {
            this.autorizacionRepository.save(autorizacion);
        }else{
            String errorMsg = "Una autorizacion con este id ya existe en la base de datos: "+ autorizacion.getAutorizacionId() ;
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(autorizacion.getAutorizacionId(), "006", "Una autorizacion con este id ya existe en la base de datos", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.getByAutorizacionNumber(autorizacion.getAutorizacionNumber());
    }

    @Override
    public Page<AutorizacionModel> getAll(Pageable pageable) {
        return this.autorizacionRepository.findAllByOrderByAutorizacionId(pageable);
    }

    @Override
    public AutorizacionModel update(AutorizacionModel autorizacion) {
        boolean existe = this.autorizacionRepository.existsById(autorizacion.getAutorizacionId());
        if(existe) {
            this.autorizacionRepository.save(autorizacion);
            return this.getByAutorizacionNumber(autorizacion.getAutorizacionNumber());
        }
        return null;
    }

    @Override
    public void delete(AutorizacionModel autorizacion) {
        this.autorizacionRepository.deleteById(autorizacion.getAutorizacionId());
    }
}
