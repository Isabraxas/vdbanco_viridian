package com.vdbanco.viridianDummy.vdbanco.funciones;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.error.ErrorSaldoInsuficiente;
import com.vdbanco.viridianDummy.funciones.ProductosClienteModel;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaOtroBancoRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaPropiaRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaTerceroRequest;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;
import com.vdbanco.viridianDummy.vdbanco.AutorizacionControllerTest;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransferenciasControllerTest {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TransferenciasControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void postTransfereciaCuentasPropias(){
        //TODO consultar primero las cuentas propias de un usuario en particular y luego con esa info mandar los parametros

        ProductosClienteModel productosClienteModel= given().pathParam("id", "42")
                .when().get("/users/{id}/productos")
                .then().extract().body().as(ProductosClienteModel.class);

        TransferenciaPropiaRequest transferenciaPropiaRequest = new TransferenciaPropiaRequest();
        transferenciaPropiaRequest.setAccountNumberOrigen(productosClienteModel.getCuentas().get(0).getAccountNumber());
        transferenciaPropiaRequest.setAccountNumberDestino(productosClienteModel.getCuentas().get(1).getAccountNumber());
        transferenciaPropiaRequest.setMonto(10.0);
        transferenciaPropiaRequest.setGlossa("Transferencia en test a cuenta propia");


                TranferenciasResponse tranferenciasResponse =
                        given().contentType("application/json")
                        .body(transferenciaPropiaRequest)
                .when().post("/users/tranferencias/propias").as(TranferenciasResponse.class);

        assertTrue(tranferenciasResponse.getEstado().equals("successful"));
    }


    @Test
    public void postTransfereciaCuentasPropiasNotWork(){
        //DO consultar primero las cuentas propias de un usuario en particular y luego con esa info mandar los parametros

        TransferenciaPropiaRequest transferenciaPropiaRequest = new TransferenciaPropiaRequest();
        transferenciaPropiaRequest.setAccountNumberOrigen("1230000003");
        transferenciaPropiaRequest.setAccountNumberDestino("1230000001");
        transferenciaPropiaRequest.setMonto(10.0);
        transferenciaPropiaRequest.setGlossa("Transferencia en test a cuenta propia");


        ErrorSaldoInsuficiente tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaPropiaRequest)
                        .when().post("/users/tranferencias/propias").as(ErrorSaldoInsuficiente.class);

        assertTrue(tranferenciasResponse.getEstado().equals("error"));
    }


    @Test
    public void postTransferenciaCuentasTerceros(){
        //DO verificar que la s cuentas sean de terceros dentro del servicio

        TransferenciaTerceroRequest transferenciaTerceroRequest = new TransferenciaTerceroRequest();
        transferenciaTerceroRequest.setAccountNumberOrigen("1230000003");
        transferenciaTerceroRequest.setAccountNumberDestino("1230000001");
        transferenciaTerceroRequest.setNombreDestinatario("Rooney Jacobs");
        transferenciaTerceroRequest.setMonto(10.0);
        transferenciaTerceroRequest.setGlossa("Transferencia en test a cuentas de terceros");

        TranferenciasResponse tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaTerceroRequest)
                        .when().post("/users/tranferencias/terceros").as(TranferenciasResponse.class);

        assertTrue(tranferenciasResponse.getEstado().equals("successful"));

    }

    @Test
    public void postTransferenciaCuentasTercerosNotWork(){
        //DO verificar que la s cuentas sean de terceros dentro del servicio

        TransferenciaTerceroRequest transferenciaTerceroRequest = new TransferenciaTerceroRequest();
        transferenciaTerceroRequest.setAccountNumberOrigen("1230000003");
        transferenciaTerceroRequest.setAccountNumberDestino("1230000002");
        transferenciaTerceroRequest.setNombreDestinatario("Rooney Jacobs");
        transferenciaTerceroRequest.setMonto(10.0);
        transferenciaTerceroRequest.setGlossa("Transferencia en test a cuentas de terceros");

        //TODO cambiar clase ErrorSaldoInsuficiente por ErrorTransferencia
        ErrorSaldoInsuficiente tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaTerceroRequest)
                        .when().post("/users/tranferencias/terceros").as(ErrorSaldoInsuficiente.class);

        assertTrue(tranferenciasResponse.getEstado().equals("error"));

    }


    @Test
    public void postTransferenciaCuentasOtrosBancos(){
        //DO verificar que la s cuentas sean de terceros dentro del servicio

        TransferenciaOtroBancoRequest transferenciaOtroBancoRequest = new TransferenciaOtroBancoRequest();
        transferenciaOtroBancoRequest.setAccountNumberOrigen("1230000003");
        transferenciaOtroBancoRequest.setAccountNumberDestino("1230000008");
        transferenciaOtroBancoRequest.setNombreDestinatario("Karyn Goodwin");
        transferenciaOtroBancoRequest.setNumeroBancoDestino("4");
        transferenciaOtroBancoRequest.setNombreBancoDestino("Banco Bisa");
        transferenciaOtroBancoRequest.setMonto(10.0);
        transferenciaOtroBancoRequest.setGlossa("Transferencia en test a cuentas para otros bancos");

        TranferenciasResponse tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaOtroBancoRequest)
                        .when().post("/users/tranferencias/terceros").as(TranferenciasResponse.class);

        assertTrue(tranferenciasResponse.getEstado().equals("successful"));

    }


    @Test
    public void postTransferenciaCuentasOtrosBancosNotWork(){
        //TODO verificar que la s cuentas sean de otros bancos

        TransferenciaOtroBancoRequest transferenciaOtroBancoRequest = new TransferenciaOtroBancoRequest();
        transferenciaOtroBancoRequest.setAccountNumberOrigen("1230000003");
        transferenciaOtroBancoRequest.setAccountNumberDestino("1230000008");
        transferenciaOtroBancoRequest.setNombreDestinatario("Rooney Jacobs");
        transferenciaOtroBancoRequest.setNumeroBancoDestino("4");
        transferenciaOtroBancoRequest.setNombreBancoDestino("Banco Bisa");
        transferenciaOtroBancoRequest.setMonto(10.0);
        transferenciaOtroBancoRequest.setGlossa("Transferencia en test a cuentas para otros bancos");

        ErrorSaldoInsuficiente tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaOtroBancoRequest)
                        .when().post("/users/tranferencias/terceros").as(ErrorSaldoInsuficiente.class);

        assertTrue(tranferenciasResponse.getEstado().equals("error"));

    }

}
