package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.transaccionRepository.findById(id);
    }

    @Override
    public List<TransaccionModel> getByTransaccionNumber(String number) {
        List<TransaccionModel> transaccion = this.transaccionRepository.findByTransaccionNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(transaccion.size()==0) {
            String errorMsg = "El transaccion con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return transaccion;
    }

    @Override
    public List<TransaccionModel> getByTransaccionByAccountNumber(String accountNumber) {
        List<TransaccionModel> transaccions = this.transaccionRepository.findByAccountNumber(accountNumber);

        if(transaccions.size()==0) {
            String errorMsg = "Las transacciones con accountNumber: "+ accountNumber +" no fueron encontradas";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(000L, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return transaccions;
    }

    @Override
    public Page<TransaccionModel> getAll(Pageable pageable) {
        return this.transaccionRepository.findAllByOrderByTransaccionId(pageable);
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
    public TransaccionModel update(TransaccionModel transaccion) {

        log.info("Revisando si exite el transaccion por number");
        //TODO tener la lista de transacciones
        //TODO hacer un foreach y comparar la cuenta para determinar que transacion debe modificarse
        //TODO tambien de debe cambiar el metodo save
        TransaccionModel currentTransaccion = this.getByTransaccionNumber(transaccion.getTransaccionNumber());

        if(currentTransaccion != null) {
            log.info("Actualizando transaccion");
            //transaccion = this.actualizarEntityTransaccion(currentTransaccion , transaccion);
            AutorizacionModel autorizacion = this.autorizacionService.getByAutorizacionNumber(transaccion.getAutorizacionAutorizacionNumber());
            if(autorizacion != null) {
                transaccion.setTransaccionId(currentTransaccion.getTransaccionId());
                transaccion.setAutorizacion(autorizacion);
                log.info("Almacenando cambios");
                this.transaccionRepository.save(transaccion);
                return this.getByTransaccionNumber(transaccion.getTransaccionNumber());
            }
        }
        return null;
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
