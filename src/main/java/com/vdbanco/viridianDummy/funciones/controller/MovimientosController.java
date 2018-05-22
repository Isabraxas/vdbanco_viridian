package com.vdbanco.viridianDummy.funciones.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vdbanco.viridianDummy.Util.Utility;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.funciones.inputModel.MovimientosConsulta;
import com.vdbanco.viridianDummy.funciones.service.MovimientosService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users")
@Api(
        name = "Movimientos",
        description = "Permite consultar por medio de una lista de metodos los movimientos realizados por una determinada cuenta en el banco.",
        stage = ApiStage.ALPHA
)
public class MovimientosController {

    private MovimientosService movimientosService;

    public MovimientosController(MovimientosService movimientosService) {
        this.movimientosService = movimientosService;
    }

    @GetMapping(value = "/{accountNumber}/movimientos")
    @ApiMethod(description = "Retorna todos los movimientos realizados por la cuenta ingresada en el path.")
    public List<TransaccionModel> getMovimientosByAccountNumber(@ApiPathParam(name = "accountNumber" ,description = "numero / codigo de cuenta") @PathVariable String accountNumber){
        return this.movimientosService.getMovimientosByAccountNumber(accountNumber);
    }

    @GetMapping(value = "/{accountNumber}/movimientos/top")
    @ApiMethod(description = "Retorna todos los 10 ultimos movimientos realizados por la cuenta ingresada en el path.")
    public List<TransaccionModel> getLastMovimientosByAccountNumber(@ApiPathParam(name = "accountNumber" ,description = "numero / codigo de cuenta") @PathVariable String accountNumber){
        return this.movimientosService.getLast10MovimientosByAccountNumber(accountNumber);
    }

    @GetMapping(value = "/{accountNumber}/movimientos/month/{numberMonth}")
    @ApiMethod(description = "Retorna todos los movimientos realizados por la cuenta ya sea en el mes presente o un mes pasado ingresando el numero de cuenta y el numero de meses a descontar en el path.")
    public List<TransaccionModel> getMovimientosByAccountNumberAndLastMonths(@ApiPathParam(name = "accountNumber" ,description = "numero / codigo de cuenta")
                                                                                 @PathVariable String accountNumber,
                                                                             @ApiPathParam(name = "numberMonth" ,description = "numero de meses a descontar ej: /0, /1, /5")
                                                                                @PathVariable Integer numberMonth ){
        return this.movimientosService.getMovimientosByAccountNumberAndLastMonths(accountNumber, numberMonth);
    }

    @GetMapping(value = "/{accountNumber}/movimientos/desde/{fechaDesde}/hasta/{fechaHasta}")
    @ApiMethod(description = "Retorna todos los movimientos realizados por la cuenta en un periodo determinado por un rango de fechas")
    public List<TransaccionModel> getMovimientosByAccountNumberAndFechas(@ApiPathParam(name = "accountNumber" ,description = "numero / codigo de cuenta")
                                                                             @PathVariable String accountNumber,
                                                                         @ApiPathParam(name = "fecha Desde" ,description = "Fecha de inicio del rango a consultar",format = "yyyy-MM-dd HH:mm:ss.SSS")
                                                                             @PathVariable Timestamp fechaDesde,
                                                                         @ApiPathParam(name = "fecha Hasta" ,description = "Fecha final del rango a consultar",format = "yyyy-MM-dd HH:mm:ss.SSS")
                                                                             @PathVariable Timestamp fechaHasta){
        return this.movimientosService.getMovimientosByAccountNumberAndFechas(accountNumber, fechaDesde, fechaHasta);
    }

    @GetMapping(value = "/{accountNumber}/movimientosP")
    @ApiMethod(description = "Retorna todos los movimientos realizados por la cuenta en un periodo de tiempo determinado por un rango de fechas. O un solo dia omitiendo el segundo parametro")
    public List<TransaccionModel> getMovimientosByAccountNumberAndParams(@ApiPathParam(name = "accountNumber" ,description = "numero / codigo de cuenta")
                                                                             @PathVariable String accountNumber,
                                                                         @ApiQueryParam(name = "fecha Desde" ,description = "Fecha de inicio del rango a consultar")
                                                                                @RequestParam(required = true) String fechaDesde,
                                                                         @ApiQueryParam(name = "fecha Hasta" ,description = "Fecha final del rango a consultar")
                                                                                @RequestParam(required = false, defaultValue = "vacio") String fechaHasta){

        if(fechaHasta.isEmpty()){
            fechaHasta = fechaDesde;
        }

        //No controlarango de horas

        Timestamp fechaA = Utility.convertStringToTimestamp(fechaDesde);


        Timestamp fechaB = Utility.convertStringToTimestamp(fechaHasta);

        return this.movimientosService.getMovimientosByAccountNumberAndFechas(accountNumber, fechaA, fechaB);
    }


}
