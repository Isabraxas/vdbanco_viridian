package com.vdbanco.viridianDummy.funciones.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vdbanco.viridianDummy.Util.Utility;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.funciones.inputModel.MovimientosConsulta;
import com.vdbanco.viridianDummy.funciones.service.MovimientosService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class MovimientosController {

    private MovimientosService movimientosService;

    public MovimientosController(MovimientosService movimientosService) {
        this.movimientosService = movimientosService;
    }

    @GetMapping(value = "/{accountNumber}/movimientos")
    public List<TransaccionModel> getMovimientosByAccountNumber(@PathVariable String accountNumber){
        return this.movimientosService.getMovimientosByAccountNumber(accountNumber);
    }

    @GetMapping(value = "/{accountNumber}/movimientos/top")
    public List<TransaccionModel> getLastMovimientosByAccountNumber(@PathVariable String accountNumber){
        return this.movimientosService.getLast10MovimientosByAccountNumber(accountNumber);
    }

    @GetMapping(value = "/{accountNumber}/movimientos/month/{numberMonth}")
    public List<TransaccionModel> getMovimientosByAccountNumberAndLastMonths(@PathVariable String accountNumber, @PathVariable Integer numberMonth ){
        return this.movimientosService.getMovimientosByAccountNumberAndLastMonths(accountNumber, numberMonth);
    }

    @GetMapping(value = "/{accountNumber}/movimientos/desde/{fechaDesde}/hasta/{fechaHasta}")
    public List<TransaccionModel> getMovimientosByAccountNumberAndFechas(@PathVariable String accountNumber
                                                                , @PathVariable Timestamp fechaDesde
                                                                , @PathVariable Timestamp fechaHasta){
        return this.movimientosService.getMovimientosByAccountNumberAndFechas(accountNumber, fechaDesde, fechaHasta);
    }

    @GetMapping(value = "/{accountNumber}/movimientosP")
    public List<TransaccionModel> getMovimientosByAccountNumberAndParams(@PathVariable String accountNumber
            , @RequestParam(required = true) String fechaDesde
            , @RequestParam(required = false, defaultValue = "vacio") String fechaHasta){

        if(fechaHasta.isEmpty()){
            fechaHasta = fechaDesde;
        }

        //No controlarango de horas

        Timestamp fechaA = Utility.convertStringToTimestamp(fechaDesde);


        Timestamp fechaB = Utility.convertStringToTimestamp(fechaHasta);

        return this.movimientosService.getMovimientosByAccountNumberAndFechas(accountNumber, fechaA, fechaB);
    }


}
