package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.*;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
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
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class TransferenciaServiceImpl implements TransferenciaService {

    private final Logger log = org.slf4j.LoggerFactory.getLogger(TransferenciaServiceImpl.class);

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

    @Autowired
    public TransferenciaServiceImpl(AccountRepository accountRepository, TransaccionRepository transaccionRepository, AutorizacionRepository autorizacionRepository, AccountService accountService, AccountHolderService accountHolderService, PersonaService personaService, AutorizacionService autorizacionService, EmpleadoService empleadoService) {
        this.accountRepository = accountRepository;
        this.transaccionRepository = transaccionRepository;
        this.autorizacionRepository = autorizacionRepository;
        this.accountService = accountService;
        this.accountHolderService = accountHolderService;
        this.personaService = personaService;
        this.autorizacionService = autorizacionService;
        this.empleadoService = empleadoService;
    }

    @Override
    public TranferenciasResponse createTranferenciaByCuentasPropias(TransferenciaPropiaRequest transferenciaPropiaRequest) {
        log.info("Buscando Cuentas");
        AccountModel accountOrigen = this.accountService.getByAccountNumber(transferenciaPropiaRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountService.getByAccountNumber(transferenciaPropiaRequest.getAccountNumberDestino());

        log.info("Comprobando si el saldo es suficiente");
        if (transferenciaPropiaRequest.getMonto() < accountOrigen.getAccountBalance()) {

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
            transaccionOrigen.setAccountNumber(transferenciaPropiaRequest.getAccountNumberOrigen());
            transaccionOrigen.setTransaccionMonto((-1)* transferenciaPropiaRequest.getMonto());
            transaccionOrigen.setTransaccionDetalle("Tranferencia cuentas propias");
            transaccionOrigen.setTransaccionGlossa(transferenciaPropiaRequest.getGlossa());
            transaccionOrigen.setTransaccionDate(fechaTransO);
            //Automatizar
            transaccionOrigen.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionOrigen.setTransaccionId(3000000004L);
            transaccionOrigen.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de origen");
            Double balanceO = accountOrigen.getAccountBalance() - transferenciaPropiaRequest.getMonto();
            accountOrigen.setAccountBalance(balanceO);
            this.accountRepository.save(accountOrigen);

            log.info("Fin proceso cuenta origen");
            //FOrigen

            log.info("Inicio proceso Cuenta destino");
            //Destino
            transaccionDestino.setAccountNumber(transferenciaPropiaRequest.getAccountNumberDestino());
            transaccionDestino.setTransaccionMonto(transferenciaPropiaRequest.getMonto());
            transaccionDestino.setTransaccionDetalle("Tranferencia cuentas propias");
            transaccionDestino.setTransaccionGlossa(transferenciaPropiaRequest.getGlossa());
            transaccionDestino.setTransaccionDate(fechaTransD);
            //Automatizar
            transaccionDestino.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionDestino.setTransaccionId(3000000005L);
            transaccionDestino.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de destino");
            Double balanceD = accountDestino.getAccountBalance() + transferenciaPropiaRequest.getMonto();
            accountDestino.setAccountBalance(balanceD);
            this.accountRepository.save(accountDestino);

            log.info("Fin proceso cuenta destino");
            //FDestino

            log.info("Guardando registro de transacciones");
            this.transaccionRepository.save(transaccionOrigen);
            this.transaccionRepository.save(transaccionDestino);
            log.info("Finalizando transaccion");

            TranferenciasResponse pagoResponse = new TranferenciasResponse();
            pagoResponse.setFecha(fechaTransO);
            pagoResponse.setEstado("successful");
            pagoResponse.setDetalle(transaccionOrigen);

            return pagoResponse;

        }else{

            String errorMsg = "La cuenta de origen no tiene saldo suficiente para este monto: "+ transferenciaPropiaRequest.getMonto() ;
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }

    }


    @Override
    public TranferenciasResponse createTranferenciaByCuentasTerceros(TransferenciaTerceroRequest transferenciaTerceroRequest) {

        log.info("Buscando Cuentas");
        AccountModel accountOrigen = this.accountService.getByAccountNumber(transferenciaTerceroRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountService.getByAccountNumber(transferenciaTerceroRequest.getAccountNumberDestino());

        log.info("Comparando datos del destinatario");
        AccountHolderModel accountHolderDestino = this.accountHolderService.getByAccountHolderNumber(accountDestino.getAccountHolderNumber());

        if ((accountHolderDestino.getAccountHolderTitular() != null &&
                accountHolderDestino.getAccountHolderTitular().equals(transferenciaTerceroRequest.getNombreDestinatario())) ||
                (accountHolderDestino.getAccountHolderApoderado() != null &&
                accountHolderDestino.getAccountHolderApoderado().equals(transferenciaTerceroRequest.getNombreDestinatario())) ||
                (accountHolderDestino.getAccountHolderFirmante() != null &&
                accountHolderDestino.getAccountHolderFirmante().equals(transferenciaTerceroRequest.getNombreDestinatario())))
        {

            log.info("Los datos corresponden correctamente");
        }else {

            String errorMsg = "En nombre en la cuenta de destino no corresponde con: " + transferenciaTerceroRequest.getNombreDestinatario();
            log.error(errorMsg);
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "003", "El nombre del destinatario no corresponde con la cuenta asociada", "Hemos encontrado un error intentelo nuevamente"));
        }

            log.info("Comprobando si el saldo es suficiente");
            if (transferenciaTerceroRequest.getMonto() < accountOrigen.getAccountBalance()) {

                log.info("Iniciando la Transaccion");
                TransaccionModel transaccionOrigen = new TransaccionModel();
                TransaccionModel transaccionDestino = new TransaccionModel();
                Timestamp fechaTransO = new Timestamp(System.currentTimeMillis());
                Timestamp fechaTransD = new Timestamp(System.currentTimeMillis());
                //Para prueba
                AutorizacionModel autorizacionTransaccion = new AutorizacionModel();
                autorizacionTransaccion.setAutorizacionNumber("AUXXXX");
                final String transaccionNumber = "T0003000000004";
                //

                log.info("Inicio proceso Cuenta origen");
                //Origen
                transaccionOrigen.setAccountNumber(transferenciaTerceroRequest.getAccountNumberOrigen());
                transaccionOrigen.setTransaccionMonto((-1) * transferenciaTerceroRequest.getMonto());
                transaccionOrigen.setTransaccionDetalle("Tranferencia cuentas de terceros");
                transaccionOrigen.setTransaccionGlossa(transferenciaTerceroRequest.getGlossa());
                transaccionOrigen.setTransaccionDate(fechaTransO);
                //Automatizar
                transaccionOrigen.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
                //transaccionOrigen.setTransaccionId(3000000004L);
                transaccionOrigen.setTransaccionNumber(transaccionNumber);

                log.info("Actualizacion de balance en la cuenta de origen");
                Double balanceO = accountOrigen.getAccountBalance() - transferenciaTerceroRequest.getMonto();
                accountOrigen.setAccountBalance(balanceO);
                this.accountRepository.save(accountOrigen);

                log.info("Fin proceso cuenta origen");
                //FOrigen

                log.info("Inicio proceso Cuenta destino");
                //Destino
                transaccionDestino.setAccountNumber(transferenciaTerceroRequest.getAccountNumberDestino());
                transaccionDestino.setTransaccionMonto(transferenciaTerceroRequest.getMonto());
                transaccionDestino.setTransaccionDetalle("Tranferencia cuentas de terceros");
                transaccionDestino.setTransaccionGlossa(transferenciaTerceroRequest.getGlossa());
                transaccionDestino.setTransaccionDate(fechaTransD);
                //Automatizar
                transaccionDestino.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
                //transaccionDestino.setTransaccionId(3000000005L);
                transaccionDestino.setTransaccionNumber(transaccionNumber);

                log.info("Actualizacion de balance en la cuenta de destino");
                Double balanceD = accountDestino.getAccountBalance() + transferenciaTerceroRequest.getMonto();
                accountDestino.setAccountBalance(balanceD);
                this.accountRepository.save(accountDestino);

                log.info("Fin proceso cuenta destino");
                //FDestino

                log.info("Guardando registro de transacciones");
                this.transaccionRepository.save(transaccionOrigen);
                this.transaccionRepository.save(transaccionDestino);
                log.info("Finalizando transaccion");

                TranferenciasResponse pagoResponse = new TranferenciasResponse();
                pagoResponse.setFecha(fechaTransO);
                pagoResponse.setEstado("successful");
                pagoResponse.setDetalle(transaccionOrigen);

                return pagoResponse;

            } else {

                String errorMsg = "La cuenta de origen no tiene saldo suficiente para este monto: " + transferenciaTerceroRequest.getMonto();
                throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
            }


    }


    @Override
    public TranferenciasResponse createTranferenciaByCuentasOtrosBancos(TransferenciaOtroBancoRequest transferenciaOtroBancoRequest) {

        log.info("Buscando Cuentas");
        AccountModel accountOrigen = this.accountService.getByAccountNumber(transferenciaOtroBancoRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountService.getByAccountNumber(transferenciaOtroBancoRequest.getAccountNumberDestino());

        log.info("Comparando datos del destinatario");
        AccountHolderModel accountHolderDestino = this.accountHolderService.getByAccountHolderNumber(accountDestino.getAccountHolderNumber());

        if((accountHolderDestino.getAccountHolderBanco().equals(transferenciaOtroBancoRequest.getNombreBancoDestino())) &&
                ( accountHolderDestino.getAccountHolderBancoNumber().equals(transferenciaOtroBancoRequest.getNumeroBancoDestino())))
        {

        }else {

            String errorMsg = "Los datos del banco de destino asociados a la cuenta son incorrectos " ;
            log.error(errorMsg);
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "004", "Los datos del banco de destino asocidos a la cuenta son incorrectos ", "Hemos encontrado un error intentelo nuevamente"));
        }

        if ((accountHolderDestino.getAccountHolderTitular() != null &&
                accountHolderDestino.getAccountHolderTitular().equals(transferenciaOtroBancoRequest.getNombreDestinatario())) ||
                (accountHolderDestino.getAccountHolderApoderado() != null &&
                        accountHolderDestino.getAccountHolderApoderado().equals(transferenciaOtroBancoRequest.getNombreDestinatario())) ||
                (accountHolderDestino.getAccountHolderFirmante() != null &&
                        accountHolderDestino.getAccountHolderFirmante().equals(transferenciaOtroBancoRequest.getNombreDestinatario())) )
        {

            log.info("Los datos corresponden correctamente");
        }else {

            String errorMsg = "En nombre en la cuenta de destino no corresponde con: " + transferenciaOtroBancoRequest.getNombreDestinatario();
            log.error(errorMsg);
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "003", "El nombre del destinatario no corresponde con la cuenta asociada", "Hemos encontrado un error intentelo nuevamente"));
        }

        log.info("Comprobando si el saldo es suficiente");
        if (transferenciaOtroBancoRequest.getMonto() < accountOrigen.getAccountBalance()) {

            log.info("Iniciando la Transaccion");
            TransaccionModel transaccionOrigen = new TransaccionModel();
            TransaccionModel transaccionDestino = new TransaccionModel();
            Timestamp fechaTransO = new Timestamp(System.currentTimeMillis());
            Timestamp fechaTransD = new Timestamp(System.currentTimeMillis());
            //Para prueba
            AutorizacionModel autorizacionTransaccion = new AutorizacionModel();
            autorizacionTransaccion.setAutorizacionNumber("AUXXXX");
            final String transaccionNumber = "T0003000000004";
            //

            log.info("Inicio proceso Cuenta origen");
            //Origen
            transaccionOrigen.setAccountNumber(transferenciaOtroBancoRequest.getAccountNumberOrigen());
            transaccionOrigen.setTransaccionMonto((-1) * transferenciaOtroBancoRequest.getMonto());
            transaccionOrigen.setTransaccionDetalle("Tranferencia cuentas en otros bancos");
            transaccionOrigen.setTransaccionGlossa(transferenciaOtroBancoRequest.getGlossa());
            transaccionOrigen.setTransaccionDate(fechaTransO);
            //Automatizar
            transaccionOrigen.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionOrigen.setTransaccionId(3000000004L);
            transaccionOrigen.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de origen");
            Double balanceO = accountOrigen.getAccountBalance() - transferenciaOtroBancoRequest.getMonto();
            accountOrigen.setAccountBalance(balanceO);
            this.accountRepository.save(accountOrigen);

            log.info("Fin proceso cuenta origen");
            //FOrigen

            log.info("Inicio proceso Cuenta destino");
            //Destino
            transaccionDestino.setAccountNumber(transferenciaOtroBancoRequest.getAccountNumberDestino());
            transaccionDestino.setTransaccionMonto(transferenciaOtroBancoRequest.getMonto());
            transaccionDestino.setTransaccionDetalle("Tranferencia cuentas en otros bancos");
            transaccionDestino.setTransaccionGlossa(transferenciaOtroBancoRequest.getGlossa());
            transaccionDestino.setTransaccionDate(fechaTransD);
            //Automatizar
            transaccionDestino.setAutorizacionNumber(autorizacionTransaccion.getAutorizacionNumber());
            //transaccionDestino.setTransaccionId(3000000005L);
            transaccionDestino.setTransaccionNumber(transaccionNumber);

            log.info("Actualizacion de balance en la cuenta de destino");
            Double balanceD = accountDestino.getAccountBalance() + transferenciaOtroBancoRequest.getMonto();
            accountDestino.setAccountBalance(balanceD);
            this.accountRepository.save(accountDestino);

            log.info("Fin proceso cuenta destino");
            //FDestino

            log.info("Guardando registro de transacciones");
            this.transaccionRepository.save(transaccionOrigen);
            this.transaccionRepository.save(transaccionDestino);
            log.info("Finalizando transaccion");

            TranferenciasResponse pagoResponse = new TranferenciasResponse();
            pagoResponse.setFecha(fechaTransO);
            pagoResponse.setEstado("successful");
            pagoResponse.setDetalle(transaccionOrigen);

            return pagoResponse;

        } else {

            String errorMsg = "La cuenta de origen no tiene saldo suficiente para este monto: " + transferenciaOtroBancoRequest.getMonto();
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
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
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
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
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }
    }

    @Override
    public TranferenciasResponse createReversionTranferencia(ReversionRequest reversionRequest) {
        List<TransaccionModel> transaccionList = transaccionRepository.findByTransaccionNumber(reversionRequest.getNumeroTransacion());
        AutorizacionModel autorizacion = autorizacionService.getByAutorizacionNumber(reversionRequest.getNumeroAutorizacion());

        //TODO crear y obtener los datos de la autorizacion referente a la reversion.
        //TODO comprobar que la fecha entre la transacion y la autorizacion no sea mayor a un dia.

        int dias=(int) ((transaccionList.get(0).getTransaccionDate().getTime() - autorizacion.getAutorizacionDateFin().getTime())/86400000);

        if(dias < 1){

            log.info("La diferencia de dias son menor 1");

            if(autorizacion.getAutorizacionType().equals("Reversion transferencia")) {

                TransaccionModel transaccionOrigen = new TransaccionModel();
                TransaccionModel transaccionDestino = new TransaccionModel();
                for (TransaccionModel transaccion : transaccionList) {
                    if (transaccion.getTransaccionMonto() < 0) {
                        transaccionOrigen = transaccion;
                    } else {
                        transaccionDestino = transaccion;
                    }
                }
                //TODO comprobar que los datos de la autorizacion correspondan con el tipo de reversion de transaccion.
                // en el autorizacion type se debe definir que tipode transaccion es

                //TODO crear una transferencia con los montos inversos.

                //Entre cuentas propias
                TransferenciaPropiaRequest transferenciaPropiaRequest = new TransferenciaPropiaRequest();
                transferenciaPropiaRequest.setAccountNumberOrigen(transaccionDestino.getAccountNumber());
                transferenciaPropiaRequest.setAccountNumberDestino(transaccionOrigen.getAccountNumber());
                transferenciaPropiaRequest.setMonto(transaccionDestino.getTransaccionMonto());
                transferenciaPropiaRequest.setGlossa(transaccionOrigen.getTransaccionGlossa());

                return this.createTranferenciaByCuentasPropias(transferenciaPropiaRequest);
            }else {

            }

        }else {

            return null;
        }

        return null;
    }

/*
    public AutorizacionModel createAutorizacionReversionTransacciones(String debitoAccountNumber,
                                                                      ){

        AutorizacionModel autorizacion = new AutorizacionModel();
        //Buscar en realidad empleado corresondiente a su cargo.
        Optional<EmpleadoModel> empleadoA = empleadoService.getById(2L);
        Optional<EmpleadoModel> empleadoB = empleadoService.getById(2L);
        Optional<EmpleadoModel> empleadoC = empleadoService.getById(2L);

        autorizacion.setAutorizacionId(30001L);
        autorizacion.setAutorizacionNumber("AU00"+autorizacion.getAutorizacionId());
        autorizacion.setEmpleadoNumber(empleadoA.get().getEmpleadoNumber());
        autorizacion.setEmpleadoNumberAuth1(empleadoB.get().getEmpleadoNumber());
        autorizacion.setEmpleadoNumberAuth2(empleadoC.get().getEmpleadoNumber());
        autorizacion.setDebitoAccountHolderNumber();
        autorizacion.setDebitoAccountNumber();
        autorizacion.setCreditoAccountHolderNumber();
        autorizacion.setAutorizacionType();
        autorizacion.setAutorizacionDateInicio();
        autorizacion.setAutorizacionDateFin();
        autorizacion.setAutorizacionDateAuth1();
        autorizacion.setAutorizacionDateAuth2();
        autorizacion.setAutorizacionDetalle();
        autorizacion.setAutorizacionGlossa();

        return null;
    }
*/

}
