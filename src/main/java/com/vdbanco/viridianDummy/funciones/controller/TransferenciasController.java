package com.vdbanco.viridianDummy.funciones.controller;

import com.vdbanco.viridianDummy.error.ErrorSaldoInsuficiente;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.inputModel.*;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
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

    @PostMapping(value = "/tranferencias/otros")
    public TranferenciasResponse createTranferenciaOtrosBancos(@RequestBody TransferenciaOtroBancoRequest transferenciaOtroBancoRequest){
        return this.transferenciaService.createTranferenciaByCuentasOtrosBancos(transferenciaOtroBancoRequest);
    }

    @PostMapping(value = "/pagos/prestamo")
    public PagoResponse createPagoPrestamo(@RequestBody PagoPrestamoRequest pagoPrestamoRequest){
        return this.transferenciaService.createPagoPrestamo(pagoPrestamoRequest);
    }

    @PostMapping(value = "/pagos/tarjetaCredito")
    public PagoResponse createPagoTarjetaCredito(@RequestBody PagoPrestamoRequest pagoPrestamoRequest){
        return this.transferenciaService.createPagoTarjetaCredito(pagoPrestamoRequest);
    }

    @PostMapping(value = "/reversion/transferencia")
    public TranferenciasResponse createReversionTranferencia(@RequestBody ReversionRequest reversionRequest){
        return this.transferenciaService.createReversionTranferencia(reversionRequest);
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
