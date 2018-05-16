package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.inputModel.*;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;
import com.vdbanco.viridianDummy.repository.AccountRepository;
import com.vdbanco.viridianDummy.repository.AutorizacionRepository;
import com.vdbanco.viridianDummy.repository.TransaccionRepository;
import com.vdbanco.viridianDummy.services.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
public class PagosServiceImpl implements PagosService {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(PagosServiceImpl.class);

    //Repositorios
    AccountRepository accountRepository;
    TransaccionRepository transaccionRepository;
    AutorizacionRepository autorizacionRepository;

    //Servicios
    AccountService accountService;
    AccountHolderService accountHolderService;
    PersonaService personaService;
    AutorizacionService autorizacionService;
    EmpleadoService empleadoService;
    TransaccionService transaccionService;

    @Autowired
    public PagosServiceImpl(AccountRepository accountRepository, TransaccionRepository transaccionRepository, AutorizacionRepository autorizacionRepository, AccountService accountService, AccountHolderService accountHolderService, PersonaService personaService, AutorizacionService autorizacionService, EmpleadoService empleadoService, TransaccionService transaccionService) {
        this.accountRepository = accountRepository;
        this.transaccionRepository = transaccionRepository;
        this.autorizacionRepository = autorizacionRepository;
        this.accountService = accountService;
        this.accountHolderService = accountHolderService;
        this.personaService = personaService;
        this.autorizacionService = autorizacionService;
        this.empleadoService = empleadoService;
        this.transaccionService = transaccionService;
    }

    @Override
    public PagoResponse createPagoServicio(PagoPrestamoRequest pagoPrestamoRequest){

        AccountModel accountOrigen = this.accountService.getByAccountNumber(pagoPrestamoRequest.getAccountNumberOrigen());
        //TODO buscar a la persona juridica con el nombre dado en el request
        //TODO buscar el account holder segun juridicas number y por ultimo encontrar la cuenta asociada
        AccountModel accountDestino = new AccountModel();

        log.info("Comprobando si el saldo es suficiente");
        if (pagoPrestamoRequest.getMonto() < accountOrigen.getAccountBalance()) {

            log.info("Iniciando la Transaccion");
            TransaccionModel transaccionOrigen = new TransaccionModel();
            TransaccionModel transaccionDestino = new TransaccionModel();
            Timestamp fechaTransO = new Timestamp(System.currentTimeMillis());
            Timestamp fechaTransD = new Timestamp(System.currentTimeMillis());
            //Para prueba
            AutorizacionModel autorizacionTransaccion = new AutorizacionModel();
            autorizacionTransaccion.setAutorizacionNumber("AUXXXX");
            final String transaccionNumber= "T0003000000004";
            //

            log.info("Inicio proceso Cuenta origen");
            //Origen
            transaccionOrigen.setAccountNumber(pagoPrestamoRequest.getAccountNumberOrigen());
            transaccionOrigen.setTransaccionMonto((-1)* pagoPrestamoRequest.getMonto());
            transaccionOrigen.setTransaccionDetalle("Pago de prestamo");
            transaccionOrigen.setTransaccionGlossa(pagoPrestamoRequest.getGlossa());
            transaccionOrigen.setTransaccionDate(fechaTransO);
            //Automatizar
            transaccionOrigen.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionOrigen.setTransaccionId(3000000004L);
            transaccionOrigen.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de origen");
            Double balanceO = accountOrigen.getAccountBalance() - pagoPrestamoRequest.getMonto();
            accountOrigen.setAccountBalance(balanceO);
            this.accountRepository.save(accountOrigen);

            log.info("Fin proceso cuenta origen");
            //FOrigen

            log.info("Inicio proceso Cuenta destino");
            //Destino
            transaccionDestino.setAccountNumber(pagoPrestamoRequest.getAccountNumberDestino());
            transaccionDestino.setTransaccionMonto(pagoPrestamoRequest.getMonto());
            transaccionDestino.setTransaccionDetalle("Pago de prestamo");
            transaccionDestino.setTransaccionGlossa(pagoPrestamoRequest.getGlossa());
            transaccionDestino.setTransaccionDate(fechaTransD);
            //Automatizar
            transaccionDestino.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionDestino.setTransaccionId(3000000005L);
            transaccionDestino.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de destino");
            Double balanceD = accountDestino.getAccountBalance() + pagoPrestamoRequest.getMonto();
            accountDestino.setAccountBalance(balanceD);
            this.accountRepository.save(accountDestino);

            log.info("Fin proceso cuenta destino");
            //FDestino

            log.info("Guardando registro de transacciones");
            this.transaccionRepository.save(transaccionOrigen);
            this.transaccionRepository.save(transaccionDestino);
            log.info("Finalizando transaccion");

            PagoResponse pagoResponse = new PagoResponse();
            pagoResponse.setFecha(fechaTransO);
            pagoResponse.setEstado("successful");
            pagoResponse.setDetalle(transaccionOrigen);

            return pagoResponse;

        }else{

            String errorMsg = "La cuenta de origen no tiene saldo suficiente para este monto: "+ pagoPrestamoRequest.getMonto() ;
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }

    }

    @Override
    public PagoResponse createPagoPrestamo(PagoPrestamoRequest pagoPrestamoRequest) {
        log.info("Buscando Cuentas");
        List<String> numeroProductos= Arrays.asList("B0007", "B0008");

        AccountModel accountOrigen = this.accountService.getByAccountNumber(pagoPrestamoRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountService.getByAccountNumberAndProductosBancarios(pagoPrestamoRequest.getAccountNumberDestino(), numeroProductos);

        log.info("Comprobando si el saldo es suficiente");
        if (pagoPrestamoRequest.getMonto() < accountOrigen.getAccountBalance()) {

            log.info("Iniciando la Transaccion");
            TransaccionModel transaccionOrigen = new TransaccionModel();
            TransaccionModel transaccionDestino = new TransaccionModel();
            Timestamp fechaTransO = new Timestamp(System.currentTimeMillis());
            Timestamp fechaTransD = new Timestamp(System.currentTimeMillis());
            //Para prueba
            AutorizacionModel autorizacionTransaccion = new AutorizacionModel();
            autorizacionTransaccion.setAutorizacionNumber("AUXXXX");
            final String transaccionNumber= "T0003000000004";
            //

            log.info("Inicio proceso Cuenta origen");
            //Origen
            transaccionOrigen.setAccountNumber(pagoPrestamoRequest.getAccountNumberOrigen());
            transaccionOrigen.setTransaccionMonto((-1)* pagoPrestamoRequest.getMonto());
            transaccionOrigen.setTransaccionDetalle("Pago de prestamo");
            transaccionOrigen.setTransaccionGlossa(pagoPrestamoRequest.getGlossa());
            transaccionOrigen.setTransaccionDate(fechaTransO);
            //Automatizar
            transaccionOrigen.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionOrigen.setTransaccionId(3000000004L);
            transaccionOrigen.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de origen");
            Double balanceO = accountOrigen.getAccountBalance() - pagoPrestamoRequest.getMonto();
            accountOrigen.setAccountBalance(balanceO);
            this.accountRepository.save(accountOrigen);

            log.info("Fin proceso cuenta origen");
            //FOrigen

            log.info("Inicio proceso Cuenta destino");
            //Destino
            transaccionDestino.setAccountNumber(pagoPrestamoRequest.getAccountNumberDestino());
            transaccionDestino.setTransaccionMonto(pagoPrestamoRequest.getMonto());
            transaccionDestino.setTransaccionDetalle("Pago de prestamo");
            transaccionDestino.setTransaccionGlossa(pagoPrestamoRequest.getGlossa());
            transaccionDestino.setTransaccionDate(fechaTransD);
            //Automatizar
            transaccionDestino.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionDestino.setTransaccionId(3000000005L);
            transaccionDestino.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de destino");
            Double balanceD = accountDestino.getAccountBalance() + pagoPrestamoRequest.getMonto();
            accountDestino.setAccountBalance(balanceD);
            this.accountRepository.save(accountDestino);

            log.info("Fin proceso cuenta destino");
            //FDestino

            log.info("Guardando registro de transacciones");
            this.transaccionRepository.save(transaccionOrigen);
            this.transaccionRepository.save(transaccionDestino);
            log.info("Finalizando transaccion");

            PagoResponse pagoResponse = new PagoResponse();
            pagoResponse.setFecha(fechaTransO);
            pagoResponse.setEstado("successful");
            pagoResponse.setDetalle(transaccionOrigen);

            return pagoResponse;

        }else{

            String errorMsg = "La cuenta de origen no tiene saldo suficiente para este monto: "+ pagoPrestamoRequest.getMonto() ;
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }

    }

    @Override
    public PagoResponse createPagoTarjetaCredito(PagoPrestamoRequest pagoPrestamoRequest) {
        log.info("Buscando Cuentas");
        List<String> numeroProductos= Arrays.asList("B0009");

        AccountModel accountOrigen = this.accountService.getByAccountNumber(pagoPrestamoRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountService.getByAccountNumberAndProductosBancarios(pagoPrestamoRequest.getAccountNumberDestino(), numeroProductos);

        log.info("Comprobando si el saldo es suficiente");
        if (pagoPrestamoRequest.getMonto() < accountOrigen.getAccountBalance()) {

            log.info("Iniciando la Transaccion");
            TransaccionModel transaccionOrigen = new TransaccionModel();
            TransaccionModel transaccionDestino = new TransaccionModel();
            Timestamp fechaTransO = new Timestamp(System.currentTimeMillis());
            Timestamp fechaTransD = new Timestamp(System.currentTimeMillis());
            //Para prueba
            AutorizacionModel autorizacionTransaccion = new AutorizacionModel();
            autorizacionTransaccion.setAutorizacionNumber("AUXXXX");
            final String transaccionNumber= "T0003000000004";
            //

            log.info("Inicio proceso Cuenta origen");
            //Origen
            transaccionOrigen.setAccountNumber(pagoPrestamoRequest.getAccountNumberOrigen());
            transaccionOrigen.setTransaccionMonto((-1)* pagoPrestamoRequest.getMonto());
            transaccionOrigen.setTransaccionDetalle("Pago de tarjeta de credito");
            transaccionOrigen.setTransaccionGlossa(pagoPrestamoRequest.getGlossa());
            transaccionOrigen.setTransaccionDate(fechaTransO);
            //Automatizar
            transaccionOrigen.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionOrigen.setTransaccionId(3000000004L);
            transaccionOrigen.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de origen");
            Double balanceO = accountOrigen.getAccountBalance() - pagoPrestamoRequest.getMonto();
            accountOrigen.setAccountBalance(balanceO);
            this.accountRepository.save(accountOrigen);

            log.info("Fin proceso cuenta origen");
            //FOrigen

            log.info("Inicio proceso Cuenta destino");
            //Destino
            transaccionDestino.setAccountNumber(pagoPrestamoRequest.getAccountNumberDestino());
            transaccionDestino.setTransaccionMonto(pagoPrestamoRequest.getMonto());
            transaccionDestino.setTransaccionDetalle("Pago de tarjeta de credito");
            transaccionDestino.setTransaccionGlossa(pagoPrestamoRequest.getGlossa());
            transaccionDestino.setTransaccionDate(fechaTransD);
            //Automatizar
            transaccionDestino.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionDestino.setTransaccionId(3000000005L);
            transaccionDestino.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de destino");
            Double balanceD = accountDestino.getAccountBalance() + pagoPrestamoRequest.getMonto();
            accountDestino.setAccountBalance(balanceD);
            this.accountRepository.save(accountDestino);

            log.info("Fin proceso cuenta destino");
            //FDestino

            log.info("Guardando registro de transacciones");
            this.transaccionRepository.save(transaccionOrigen);
            this.transaccionRepository.save(transaccionDestino);
            log.info("Finalizando transaccion");

            PagoResponse pagoResponse = new PagoResponse();
            pagoResponse.setFecha(fechaTransO);
            pagoResponse.setEstado("successful");
            pagoResponse.setDetalle(transaccionOrigen);

            return pagoResponse;

        }else{

            String errorMsg = "La cuenta de origen no tiene saldo suficiente para este monto: "+ pagoPrestamoRequest.getMonto() ;
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }
    }


}
