package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.TransaccionRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TransaccionServiceImpl.class);

    private TransaccionRepository transaccionRepository;
    private AutorizacionService autorizacionService;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository, AutorizacionService autorizacionService) {
        this.transaccionRepository = transaccionRepository;
        this.autorizacionService = autorizacionService;
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
        
        log.info("Revisando si exite el transaccion por number");
        List<TransaccionModel> transaccions = this.transaccionRepository.findByTransaccionNumber(transaccion.getTransaccionNumber());
        
        
        for (TransaccionModel transaccionModel : transaccions) {
            
            if(transaccionModel == null) {
                log.info("Creando transaccion");
                AutorizacionModel autorizacion = this.autorizacionService.getByAutorizacionNumber(transaccion.getAutorizacionNumber());
                if(autorizacion != null) {
                    transaccion.setAutorizacion(autorizacion);
                    log.info("Almacenando  transaccion");
                    return this.transaccionRepository.save(transaccion);
                }
            }else{
                log.error("El transaccion con number: "+ transaccion.getTransaccionNumber() +" ya existe");
                String errorMsg = "El transaccion con number: "+ transaccion.getTransaccionNumber() +" ya existe";
                throw new ConflictsException(errorMsg, new ErrorDetalle(transaccion.getTransaccionId(),"409","El transaccion con number: "+ transaccion.getTransaccionNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
            }
        }
        
        return null;
    }
    
    @Override
    public TransaccionModel update(TransaccionModel transaccion) {

        log.info("Revisando si exite el transaccion por number");
        List<TransaccionModel> currentTransaccions = this.getByTransaccionNumber(transaccion.getTransaccionNumber());
        TransaccionModel currentTransaccion= new TransaccionModel();
        for (TransaccionModel myTransaccion:currentTransaccions) {
            if(myTransaccion.getAccountNumber() == transaccion.getAccountNumber()){
                currentTransaccion = myTransaccion;
            }
        }
        if(currentTransaccion != null) {
            log.info("Actualizando transaccion");
            //transaccion = this.actualizarEntityTransaccion(currentTransaccion , transaccion);
            AutorizacionModel autorizacion = this.autorizacionService.getByAutorizacionNumber(transaccion.getAutorizacionNumber());
            if(autorizacion != null) {
                transaccion.setTransaccionId(currentTransaccion.getTransaccionId());
                transaccion.setAutorizacion(autorizacion);
                log.info("Almacenando cambios");
                return this.transaccionRepository.save(transaccion);
                //TODO creo que este metodo deberia devlover las 2 transacciones relacionadas al number y en los servicios funcionales
                //TODO deberia devolverse solo el currespondiente a x cuenta
            }
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
