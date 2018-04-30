package com.vdbanco.viridianDummy.funciones.controller;

import com.vdbanco.viridianDummy.error.ErrorSaldoInsuficiente;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaPropiaRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaTerceroRequest;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;
import com.vdbanco.viridianDummy.funciones.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping(value = "/users")
public class TransferenciasController {

    private TransferenciaService transferenciaService;

    @Autowired
    public TransferenciasController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping(value = "/tranferencias/propias")
    public TranferenciasResponse createTranferenciaPropias(@RequestBody TransferenciaPropiaRequest transferenciaPropiaRequest){
        return this.transferenciaService.createTranferenciaByCuentasPropias(transferenciaPropiaRequest);
    }

    @PostMapping(value = "/tranferencias/terceros")
    public TranferenciasResponse createTranferenciaTerceros(@RequestBody TransferenciaTerceroRequest transferenciaTerceroRequest){
        return this.transferenciaService.createTranferenciaByCuentasTerceros(transferenciaTerceroRequest);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoEncontradoRestException.class)
    public ErrorSaldoInsuficiente handleNotFound(NoEncontradoRestException exception){
        Timestamp fecha = new Timestamp(System.currentTimeMillis());
        ErrorSaldoInsuficiente error = new ErrorSaldoInsuficiente();

        error.setFecha(fecha);
        error.setEstado("error");
        error.setError(exception.getErrorNoEncontrado());
        return error;
    }
}
