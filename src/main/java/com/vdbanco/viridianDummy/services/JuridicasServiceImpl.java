package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.JuridicasModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.JuridicasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service    
public class JuridicasServiceImpl implements JuridicasService {
    private JuridicasRepository juridicasRepository;

    @Autowired
    public JuridicasServiceImpl(JuridicasRepository juridicasRepository) {
        this.juridicasRepository = juridicasRepository;
    }

    @Override
    public Optional<JuridicasModel> getById(Long id) {
        Optional<JuridicasModel> juridicas = this.juridicasRepository.findById(id);
        if(!juridicas.isPresent()) {
            String errorMsg = "El juridicas con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.juridicasRepository.findById(id);
    }

    @Override
    public JuridicasModel getByJuridicasNumber(String number) {
        JuridicasModel juridicas = this.juridicasRepository.findByJuridicasNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(juridicas == null) {
            String errorMsg = "El juridicas con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return juridicas;
    }

    @Override
    public JuridicasModel save(JuridicasModel juridicas) {
        boolean existe = this.juridicasRepository.existsById(juridicas.getJuridicasId());
        if(!existe) {
            this.juridicasRepository.save(juridicas);
        }
        return this.getByJuridicasNumber(juridicas.getJuridicasNumber());
    }

    @Override
    public Page<JuridicasModel> getAll(Pageable pageable) {
        return this.juridicasRepository.findAllByOrderByJuridicasId(pageable);
    }

    @Override
    public JuridicasModel update(JuridicasModel juridicas) {
        boolean existe = this.juridicasRepository.existsById(juridicas.getJuridicasId());
        if(existe) {
            this.juridicasRepository.save(juridicas);
            return this.getByJuridicasNumber(juridicas.getJuridicasNumber());
        }
        return null;
    }

    @Override
    public void delete(JuridicasModel juridicas) {
        this.juridicasRepository.deleteById(juridicas.getJuridicasId());
    }
}
