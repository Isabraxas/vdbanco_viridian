package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.TransaccionModel;

import java.sql.Timestamp;
import java.util.List;

public interface MovimientosService {

    List<TransaccionModel> getMovimientosByAccountNumber(String accountNumber);

    List<TransaccionModel> getLast10MovimientosByAccountNumber(String accountNumber);

    List<TransaccionModel> getMovimientosByAccountNumberAndFechas(String accountNumber, Timestamp fechaDesde, Timestamp fechaHasta);
}
