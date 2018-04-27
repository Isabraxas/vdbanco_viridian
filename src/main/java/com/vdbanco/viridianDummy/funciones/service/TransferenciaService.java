package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciasRequest;

import java.util.List;

public interface TransferenciaService {
     List<TransaccionModel> createTranferenciaByCuentasPropias(TransferenciasRequest transferenciasRequest);
}
