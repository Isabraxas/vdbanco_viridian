package com.vdbanco.viridianDummy.funciones.controller;

import com.vdbanco.viridianDummy.error.ErrorTransferencia;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.inputModel.*;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;
import com.vdbanco.viridianDummy.funciones.service.TransferenciaService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

@RestController
@RequestMapping(value = "/api/users")
@Api(
        name = "Tranferencias",
        description ="Permite realizar transferencias entre cuentas por medio de una lista de metodos.",
        stage = ApiStage.ALPHA
)
public class TransferenciasController {

    private TransferenciaService transferenciaService;

    @Autowired
    public TransferenciasController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tranferencias/propias")
    @ApiMethod(description = "Realiza transferencias entre cuentas propias  proporcionando los siguientes datos de entrada (cuenta origen, numero de cuenta/préstamo a pagar, monto, descripción/glosa , autorización).")
    public TranferenciasResponse createTranferenciaPropias(@Valid @ApiBodyObject @RequestBody TransferenciaPropiaRequest transferenciaPropiaRequest){
        return this.transferenciaService.createTranferenciaByCuentasPropias(transferenciaPropiaRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tranferencias/terceros")
    @ApiMethod(description = "Realiza transferencias a cuentas de terceros proporcionando los siguientes datos de entrada (cuenta origen, numero de cuenta/préstamo a pagar, nombre del destinatario, monto, descripción/glosa , autorización).")
    public TranferenciasResponse createTranferenciaTerceros(@Valid @RequestBody TransferenciaTerceroRequest transferenciaTerceroRequest){
        return this.transferenciaService.createTranferenciaByCuentasTerceros(transferenciaTerceroRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tranferencias/otros")
    @ApiMethod(description = "Realiza transferencias a cuentas de terceros proporcionando los siguientes datos de entrada (cuenta origen, numero de cuenta/préstamo a pagar, nombre del destinatario,nombre del banco de destino, numero del banco de destino, monto, descripción/glosa , autorización).")
    public TranferenciasResponse createTranferenciaOtrosBancos(@Valid @RequestBody TransferenciaOtroBancoRequest transferenciaOtroBancoRequest){
        return this.transferenciaService.createTranferenciaByCuentasOtrosBancos(transferenciaOtroBancoRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reversion/transferencia")
    @ApiMethod(description = "Realiza la reversion de una determinada transaccion proporcionado el numero de transaccion el numero de la debida autorizacion prar revertirla.")
    public TranferenciasResponse createReversionTranferencia(@Valid @RequestBody ReversionRequest reversionRequest){
        return this.transferenciaService.createReversionTransferencia(reversionRequest);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoEncontradoRestException.class)
    public ErrorTransferencia handleNotFound(NoEncontradoRestException exception){
        Timestamp fecha = new Timestamp(System.currentTimeMillis());
        ErrorTransferencia error = new ErrorTransferencia();

        error.setFecha(fecha);
        error.setEstado("error");
        error.setError(exception.getErrorDetalle());
        return error;
    }
}
