package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.*;
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
    TransaccionService transaccionService;

    @Autowired
    public TransferenciaServiceImpl(AccountRepository accountRepository, TransaccionRepository transaccionRepository, AutorizacionRepository autorizacionRepository, AccountService accountService, AccountHolderService accountHolderService, PersonaService personaService, AutorizacionService autorizacionService, EmpleadoService empleadoService, TransaccionService transaccionService) {
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
    public TranferenciasResponse createTranferenciaByCuentasPropias(TransferenciaPropiaRequest transferenciaPropiaRequest) {
        log.info("Buscando Cuentas");
        AccountModel accountOrigen = this.accountService.getByAccountNumber(transferenciaPropiaRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountService.getByAccountNumber(transferenciaPropiaRequest.getAccountNumberDestino());

        log.info("Comprobando propiedad de las cuentas");
        if(accountOrigen.getAccountHolderNumber().equals(accountDestino.getAccountHolderNumber())){

            log.info("Son cuentas propias");

        }else{

            String errorMsg = "Las cuentas no pertenecen al mismo propiertario: "+ accountOrigen.getAccountNumber() + " - "+ accountDestino.getAccountNumber();
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "Las cuentas no pertenecen al mismo propiertario: "+ accountOrigen.getAccountNumber() + " - "+ accountDestino.getAccountNumber(), "Hemos encontrado un error intentelo nuevamente"));

        }

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

            TransaccionModel lastTransaccion = this.transaccionService.getLastTransaccion();
            final String transaccionNumber = "T000"+(Long.valueOf(lastTransaccion.getTransaccionNumber().substring(4)) + 1 );
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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }

    }



    public TranferenciasResponse createTranferencia(TransferenciaReversionRequest transferenciaPropiaRequest) {
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

            TransaccionModel lastTransaccion = this.transaccionService.getLastTransaccion();
            final String transaccionNumber = "T000"+(Long.valueOf(lastTransaccion.getTransaccionNumber().substring(4)) + 1 );
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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }

    }

    @Override
    public TranferenciasResponse createTranferenciaByCuentasTerceros(TransferenciaTerceroRequest transferenciaTerceroRequest) {

        log.info("Buscando Cuentas");
        AccountModel accountOrigen = this.accountService.getByAccountNumber(transferenciaTerceroRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountService.getByAccountNumber(transferenciaTerceroRequest.getAccountNumberDestino());

        log.info("Comprobando propiedad de las cuentas");
        if(accountOrigen.getAccountHolderNumber().equals(accountDestino.getAccountHolderNumber())){

            String errorMsg = "No son cuentas de terceros: "+ accountOrigen.getAccountNumber() + " - "+ accountDestino.getAccountNumber();
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "Las cuentas no pertenecen a terceros: "+ accountOrigen.getAccountNumber() + " - "+ accountDestino.getAccountNumber(), "Hemos encontrado un error intentelo nuevamente"));

        }else{
            log.info("No son cuentas propias");

        }


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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "003", "El nombre del destinatario no corresponde con la cuenta asociada", "Hemos encontrado un error intentelo nuevamente"));
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
                throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "004", "Los datos del banco de destino asocidos a la cuenta son incorrectos ", "Hemos encontrado un error intentelo nuevamente"));
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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "003", "El nombre del destinatario no corresponde con la cuenta asociada", "Hemos encontrado un error intentelo nuevamente"));
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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
        }


    }

    @Override
    public PagoResponse createPagoServicio(PagoPrestamoRequest pagoPrestamoRequest){

        AccountModel accountOrigen = this.accountService.getByAccountNumber(pagoPrestamoRequest.getAccountNumberOrigen());
        //TODO buscar a la persona juridica con el nombre dado en el request
        //TODO buscar el account hollder segun juridicas number y por ultimo encontrar la cuenta asociada
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

    @Override
    public TranferenciasResponse createReversionTransferencia(ReversionRequest reversionRequest) {

        List<TransaccionModel> transaccionList = transaccionRepository.findByTransaccionNumber(reversionRequest.getNumeroTransacion());
        AutorizacionModel autorizacion = autorizacionService.getByAutorizacionNumber(reversionRequest.getNumeroAutorizacion());

        //comprobar tanto que tarnasacciones con ese numero como autorizacion existen
        // la autorizacion debe crearce aparte no se si a mano o con un metodo especifico para esta
        //comprobar que la uatorizacion type es para una reversion
        // realizar la reversion
        //IMPORTANTE: El servicio encargado de comprobar que la reversion no se aplique mas de una vez sobre una transaccion
        // y tambien de que no se aplique sobre una reversion.
        if (!transaccionList.isEmpty() && autorizacion != null) {

            int dias = (int) ((transaccionList.get(0).getTransaccionDate().getTime() - autorizacion.getAutorizacionDateFin().getTime()) / 86400000);

            log.info("La diferencia de dias son menor 1");
            if (dias < 1) {

                log.info("Comprobando el origen y el destino");
                TransaccionModel transaccionOrigen = new TransaccionModel();
                TransaccionModel transaccionDestino = new TransaccionModel();
                for (TransaccionModel transaccion : transaccionList) {
                    if (transaccion.getTransaccionMonto() < 0) {
                        transaccionOrigen = transaccion;
                    } else {
                        transaccionDestino = transaccion;
                    }
                }

                if (autorizacion.getAutorizacionType().equals("Reversion")) {
                    TransferenciaReversionRequest transferenciaReversionRequest = new TransferenciaReversionRequest();

                    transferenciaReversionRequest.setAccountNumberOrigen(transaccionDestino.getAccountNumber());
                    transferenciaReversionRequest.setAccountNumberDestino(transaccionOrigen.getAccountNumber());
                    transferenciaReversionRequest.setMonto(transaccionDestino.getTransaccionMonto());
                    transferenciaReversionRequest.setGlossa("Reversion de transaccion numero:" + reversionRequest.getNumeroTransacion());

                    return  this.createTranferencia(transferenciaReversionRequest);
                }
            }


        }
        return null;
    }

/*
    public AutorizacionModel createAutorizacionReversionTransacciones(String accountNumberOrigen, String accountNumberDestino ){
        AccountModel accountOrigen = this.accountService.getByAccountNumber(accountNumberOrigen);
        AccountModel accountDestino = this.accountService.getByAccountNumber(accountNumberDestino);

        AccountHolderModel accountHolderDebito = this.accountHolderService.getByAccountHolderNumber(accountOrigen.getAccountHolderNumber());
        AccountHolderModel accountHolderCredito = this.accountHolderService.getByAccountHolderNumber(accountDestino.getAccountHolderNumber());

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
        autorizacion.setDebitoAccountHolderNumber(accountDestino.getAccountHolderNumber());
        autorizacion.setDebitoAccountNumber(accountOrigen.getAccountNumber());
        autorizacion.setCreditoAccountHolderNumber(accountOrigen.getAccountHolderNumber());
        autorizacion.setAutorizacionType("Reversion");
        autorizacion.setAutorizacionDateInicio(new Timestamp(System.currentTimeMillis()));
        autorizacion.setAutorizacionDateFin(new Timestamp(System.currentTimeMillis()));
        autorizacion.setAutorizacionDateAuth1();
        autorizacion.setAutorizacionDateAuth2();
        autorizacion.setAutorizacionDetalle();
        autorizacion.setAutorizacionGlossa();

        return null;
    }
*/


}
