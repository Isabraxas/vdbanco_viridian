package com.vdbanco.viridianDummy.funciones.service;


import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.repository.TransaccionRepository;
import com.vdbanco.viridianDummy.services.AutorizacionService;
import com.vdbanco.viridianDummy.services.TransaccionService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class MovimientosServiceImpl implements MovimientosService{

    //Servicios
    private TransaccionService transaccionService;
    private AutorizacionService autorizacionService;

    //Repositorios
    private TransaccionRepository transaccionRepository;

    public MovimientosServiceImpl(TransaccionService transaccionService, AutorizacionService autorizacionService, TransaccionRepository transaccionRepository) {
        this.transaccionService = transaccionService;
        this.autorizacionService = autorizacionService;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public List<TransaccionModel> getMovimientosByAccountNumber(String accountNumber) {
        return transaccionService.getByTransaccionByAccountNumber(accountNumber);
    }

    @Override
    public List<TransaccionModel> getLast10MovimientosByAccountNumber(String accountNumber) {
        return transaccionRepository.findTop10ByAccountNumberOrderByTransaccionDateDesc(accountNumber);
    }

    @Override
    public List<TransaccionModel> getMovimientosByAccountNumberAndFechas(String accountNumber, Timestamp fechaDesde, Timestamp fechaHasta) {
        return transaccionRepository.findByAccountNumberAndTransaccionDateBetween(accountNumber, fechaDesde, fechaHasta);
    }
}
