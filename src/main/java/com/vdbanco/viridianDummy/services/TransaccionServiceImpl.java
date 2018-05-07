package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    private TransaccionRepository transaccionRepository;

    @Autowired
    public TransaccionServiceImpl(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public Optional<TransaccionModel> getById(Long id) {
        Optional<TransaccionModel> transaccion = this.transaccionRepository.findById(id);
        if(!transaccion.isPresent()) {
            String errorMsg = "El transaccion con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.transaccionRepository.findById(id);
    }

    @Override
    public List<TransaccionModel> getByTransaccionNumber(String number) {
        List<TransaccionModel> transaccion = this.transaccionRepository.findByTransaccionNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(transaccion.size()==0) {
            String errorMsg = "El transaccion con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return transaccion;
    }

    @Override
    public List<TransaccionModel> getByTransaccionByAccountNumber(String accountNumber) {
        List<TransaccionModel> transaccions = this.transaccionRepository.findByAccountNumber(accountNumber);

        if(transaccions.size()==0) {
            String errorMsg = "Las transacciones con accountNumber: "+ accountNumber +" no fueron encontradas";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(000L, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return transaccions;
    }

    @Override
    public TransaccionModel save(TransaccionModel transaccion) {
        boolean existe = this.transaccionRepository.existsById(transaccion.getTransaccionId());
        if(!existe) {
            return this.transaccionRepository.save(transaccion);
        }
        return null;
    }

    @Override
    public Page<TransaccionModel> getAll(Pageable pageable) {
        return this.transaccionRepository.findAllByOrderByTransaccionId(pageable);
    }

    @Override
    public TransaccionModel update(TransaccionModel transaccion) {
        boolean existe = this.transaccionRepository.existsById(transaccion.getTransaccionId());
        if(existe) {

            return this.transaccionRepository.save(transaccion);
        }
        return null;
    }

    @Override
    public void delete(TransaccionModel transaccion) {
        this.transaccionRepository.deleteById(transaccion.getTransaccionId());
    }

    @Override
    public TransaccionModel getLastTransaccion(){
        return this.transaccionRepository.findFirstByOrderByAccountIdDesc();
    }
}
