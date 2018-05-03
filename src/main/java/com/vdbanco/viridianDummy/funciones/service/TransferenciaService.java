package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.funciones.inputModel.*;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;

public interface TransferenciaService {
     TranferenciasResponse createTranferenciaByCuentasPropias(TransferenciaPropiaRequest transferenciaPropiaRequest);

    TranferenciasResponse createTranferenciaByCuentasTerceros(TransferenciaTerceroRequest transferenciaTerceroRequest);

    TranferenciasResponse createTranferenciaByCuentasOtrosBancos(TransferenciaOtroBancoRequest transferenciaOtroBancoRequest);

    PagoResponse createPagoServicio(PagoPrestamoRequest pagoPrestamoRequest);

    PagoResponse createPagoPrestamo(PagoPrestamoRequest pagoPrestamoRequest);

    PagoResponse createPagoTarjetaCredito(PagoPrestamoRequest pagoPrestamoRequest);

    TranferenciasResponse createReversionTranferencia(ReversionRequest reversionRequest);
}
