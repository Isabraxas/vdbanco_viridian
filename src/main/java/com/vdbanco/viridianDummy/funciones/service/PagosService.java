package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.funciones.inputModel.*;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;

public interface PagosService {

    PagoResponse createPagoServicio(PagoPrestamoRequest pagoPrestamoRequest);

    PagoResponse createPagoPrestamo(PagoPrestamoRequest pagoPrestamoRequest);

    PagoResponse createPagoTarjetaCredito(PagoPrestamoRequest pagoPrestamoRequest);

}
