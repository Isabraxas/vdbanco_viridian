package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaPropiaRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaTerceroRequest;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;

public interface TransferenciaService {
     TranferenciasResponse createTranferenciaByCuentasPropias(TransferenciaPropiaRequest transferenciaPropiaRequest);

    TranferenciasResponse createTranferenciaByCuentasTerceros(TransferenciaTerceroRequest transferenciaTerceroRequest);
}
