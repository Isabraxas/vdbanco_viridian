package com.vdbanco.viridianDummy.funciones.service;

import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciasRequest;
import com.vdbanco.viridianDummy.repository.AccountRepository;
import com.vdbanco.viridianDummy.repository.AutorizacionRepository;
import com.vdbanco.viridianDummy.repository.TransaccionRepository;

import java.util.List;

public class TransferenciaServiceImpl implements TransferenciaService {

    //Repositorios
    AccountRepository accountRepository;
    TransaccionRepository transaccionRepository;
    AutorizacionRepository autorizacionRepository;

    public TransferenciaServiceImpl(AccountRepository accountRepository, TransaccionRepository transaccionRepository, AutorizacionRepository autorizacionRepository) {
        this.accountRepository = accountRepository;
        this.transaccionRepository = transaccionRepository;
        this.autorizacionRepository = autorizacionRepository;
    }

    @Override
    public List<TransaccionModel> createTranferenciaByCuentasPropias(TransferenciasRequest transferenciasRequest) {

        AccountModel accountOrigen = this.accountRepository.findByAccountNumber(transferenciasRequest.getAccountNumberOrigen());
        AccountModel accountDestino = this.accountRepository.findByAccountNumber(transferenciasRequest.getAccountNumberDestino());


        TransaccionModel transaccionOrigen = new TransaccionModel();
        TransaccionModel transaccionDestino = new TransaccionModel();

        transaccionOrigen.setAccountNumber(transferenciasRequest.getAccountNumberOrigen());
        transaccionOrigen.setTransaccionMonto(transferenciasRequest.getMonto());
        transaccionOrigen.setTransaccionDetalle("Tranferencia cuentas propias");
        transaccionOrigen.setTransaccionGlossa(transferenciasRequest.getGlossa());
        //Automatizar
        transaccionOrigen.setAutorizacionNumber("AUXXXX");
        transaccionOrigen.setTransaccionId(3000000004L);
        transaccionOrigen.setTransaccionNumber("T0003000000004");

        transaccionDestino.setAccountNumber(transferenciasRequest.getAccountNumberDestino());
        transaccionDestino.setTransaccionMonto(transferenciasRequest.getMonto());
        transaccionDestino.setTransaccionDetalle("Tranferencia cuentas propias");
        transaccionDestino.setTransaccionGlossa(transferenciasRequest.getGlossa());
        //Automatizar
        transaccionOrigen.setAutorizacionNumber("AUXXXX");
        transaccionOrigen.setTransaccionId(3000000005L);
        transaccionOrigen.setTransaccionNumber("T0003000000005");



        return null;
    }
}
