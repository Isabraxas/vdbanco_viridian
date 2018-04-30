package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaPropiaRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaTerceroRequest;
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

            TranferenciasResponse tranferenciasResponse = new TranferenciasResponse();
            tranferenciasResponse.setFecha(fechaTransO);
            tranferenciasResponse.setEstado("successful");
            tranferenciasResponse.setDetalle(transaccionOrigen);

            return tranferenciasResponse;

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

                TranferenciasResponse tranferenciasResponse = new TranferenciasResponse();
                tranferenciasResponse.setFecha(fechaTransO);
                tranferenciasResponse.setEstado("successful");
                tranferenciasResponse.setDetalle(transaccionOrigen);

                return tranferenciasResponse;

            } else {

                String errorMsg = "La cuenta de origen no tiene saldo suficiente para este monto: " + transferenciaTerceroRequest.getMonto();
                throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(accountOrigen.getAccountId(), "002", "El saldo es insuficiente para procesar la transferencia", "Hemos encontrado un error intentelo nuevamente"));
            }


    }
}
