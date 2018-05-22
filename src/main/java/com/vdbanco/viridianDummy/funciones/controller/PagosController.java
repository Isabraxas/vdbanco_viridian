package com.vdbanco.viridianDummy.funciones.controller;

import com.vdbanco.viridianDummy.error.ErrorTransferencia;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.inputModel.*;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;
import com.vdbanco.viridianDummy.funciones.service.PagosService;
import com.vdbanco.viridianDummy.funciones.service.PagosService;
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
        name = "Pagos",
        description ="Permite realizar pagos por medio de una lista de metodos.",
        stage = ApiStage.ALPHA
)
public class PagosController {

    private PagosService pagosService;

    @Autowired
    public PagosController(PagosService pagosService) {
        this.pagosService = pagosService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pagos/servicio")
    @ApiMethod(description = "Realiza el pago de servicio que requiere como datos de entrada (cuenta origen, numero de cuenta/préstamo a pagar, monto, descripción/glosa , autorización)")
    public PagoResponse createPagoTarjetaCredito(@Valid @RequestBody PagoServicioRequest pagoServicioRequest){
        return this.pagosService.createPagoServicio(pagoServicioRequest);
    }

    //TODO falta agregar autorizacion
    @RequestMapping(method = RequestMethod.POST , value = "/pagos/prestamo")
    @ApiMethod(description = "Realiza el pago de prestamo que requiere como datos de entrada (cuenta origen, numero de cuenta/préstamo a pagar, monto, descripción/glosa , autorización)")
    public PagoResponse createPagoPrestamo(@ApiBodyObject @Valid  @RequestBody PagoPrestamoRequest pagoPrestamoRequest){
        return this.pagosService.createPagoPrestamo(pagoPrestamoRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/pagos/tarjetaCredito")
    @ApiMethod(description = "Realiza el pago de tarjeta de credito que requiere como datos de entrada (cuenta origen, numero de cuenta/préstamo a pagar, monto, descripción/glosa , autorización)")
    public PagoResponse createPagoTarjetaCredito(@Valid @RequestBody PagoPrestamoRequest pagoPrestamoRequest){
        return this.pagosService.createPagoTarjetaCredito(pagoPrestamoRequest);
    }


    //TODO falta controller pagos servicio


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
