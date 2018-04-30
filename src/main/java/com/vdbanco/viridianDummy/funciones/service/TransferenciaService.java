package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.funciones.inputModel.PagoPrestamoRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaOtroBancoRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaPropiaRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaTerceroRequest;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;

public interface TransferenciaService {
     TranferenciasResponse createTranferenciaByCuentasPropias(TransferenciaPropiaRequest transferenciaPropiaRequest);

    TranferenciasResponse createTranferenciaByCuentasTerceros(TransferenciaTerceroRequest transferenciaTerceroRequest);

    TranferenciasResponse createTranferenciaByCuentasOtrosBancos(TransferenciaOtroBancoRequest transferenciaOtroBancoRequest);

    PagoResponse createPagoPrestamo(PagoPrestamoRequest pagoPrestamoRequest);
}
