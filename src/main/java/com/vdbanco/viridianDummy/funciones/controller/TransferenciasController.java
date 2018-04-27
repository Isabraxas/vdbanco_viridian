package com.vdbanco.viridianDummy.funciones.controller;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciasRequest;
import com.vdbanco.viridianDummy.funciones.service.TransferenciaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class TransferenciasController {
    private TransferenciaService transferenciaService;

    public TransferenciasController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping(value = "/tranferencias/propias")
    public List<TransaccionModel> createTranferenciaPropias(@RequestBody TransferenciasRequest transferenciasRequest){
        return this.transferenciaService.createTranferenciaByCuentasPropias(transferenciasRequest);
    }
}
