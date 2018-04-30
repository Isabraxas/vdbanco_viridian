package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.inputModel.PagoPrestamoRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaOtroBancoRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaPropiaRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaTerceroRequest;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;
import com.vdbanco.viridianDummy.repository.AccountRepository;
import com.vdbanco.viridianDummy.repository.AutorizacionRepository;
import com.vdbanco.viridianDummy.repository.TransaccionRepository;
import com.vdbanco.viridianDummy.services.AccountHolderService;
import com.vdbanco.viridianDummy.services.AccountService;
import com.vdbanco.viridianDummy.services.PersonaService;
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

    @Autowired
    public TransferenciaServiceImpl(AccountRepository accountRepository, TransaccionRepository transaccionRepository, AutorizacionRepository autorizacionRepository, AccountService accountService, AccountHolderService accountHolderService, PersonaService personaService) {
        this.accountRepository = accountRepository;
        this.transaccionRepository = transaccionRepository;
        this.autorizacionRepository = autorizacionRepository;
        this.accountService = accountService;
        this.accountHolderService = accountHolderService;
        this.personaService = personaService;
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
                transaccionOrigen.setTransaccionDetalle("Tranferencia cuentas propias");
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
                transaccionDestino.setTransaccionDetalle("Tranferencia cuentas propias");
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
            transaccionOrigen.setTransaccionDetalle("Tranferencia cuentas propias");
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
            transaccionDestino.setTransaccionDetalle("Tranferencia cuentas propias");
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
        return null;
    }
/*
    @Override
    public PagoResponse createPagoPrestamo(PagoPrestamoRequest pagoPrestamoRequest){
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
            transaccionOrigen.setTransaccionDetalle("Tranferencia cuentas propias");
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
            transaccionDestino.setTransaccionDetalle("Tranferencia cuentas propias");
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
*/

}
