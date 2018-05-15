package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.funciones.inputModel.*;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;

public interface TransferenciaService {
     TranferenciasResponse createTranferenciaByCuentasPropias(TransferenciaPropiaRequest transferenciaPropiaRequest);

    TranferenciasResponse createTranferenciaByCuentasTerceros(TransferenciaTerceroRequest transferenciaTerceroRequest);

    TranferenciasResponse createTranferenciaByCuentasOtrosBancos(TransferenciaOtroBancoRequest transferenciaOtroBancoRequest);

    TranferenciasResponse createReversionTransferencia(ReversionRequest reversionRequest);
}
