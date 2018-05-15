package com.vdbanco.viridianDummy.vdbanco.funciones;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.error.ErrorTransferencia;
import com.vdbanco.viridianDummy.funciones.ProductosClienteModel;
import com.vdbanco.viridianDummy.funciones.inputModel.ReversionRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaOtroBancoRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaPropiaRequest;
import com.vdbanco.viridianDummy.funciones.inputModel.TransferenciaTerceroRequest;
import com.vdbanco.viridianDummy.funciones.outputModel.TranferenciasResponse;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.authentication;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.*;

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
        //DO consultar primero las cuentas propias de un usuario en particular y luego con esa info mandar los parametros

        ProductosClienteModel productosClienteModel= given().pathParam("id", "42")
              .when().get("/users/{id}/productos")
                .then().extract().body().as(ProductosClienteModel.class);


        TransferenciaPropiaRequest transferenciaPropiaRequest = new TransferenciaPropiaRequest();
        transferenciaPropiaRequest.setAccountNumberOrigen(productosClienteModel.getCuentas().get(0).getAccountNumber());
        transferenciaPropiaRequest.setAccountNumberDestino(productosClienteModel.getCuentas().get(1).getAccountNumber());
        transferenciaPropiaRequest.setMonto(10.0);
        transferenciaPropiaRequest.setGlossa("Transferencia en test a cuenta propia");


                        given().contentType("application/json")
                        .body(transferenciaPropiaRequest)
                        .when().post("/users/tranferencias/propias").then()
                                .statusCode(200)
                                .body("estado",equalTo("successful"));

        //assertTrue(tranferenciasResponse.getEstado().equals("successful"));
    }


    @Test
    public void postTransfereciaCuentasPropiasNotWork(){
        //DO consultar primero las cuentas propias de un usuario en particular y luego con esa info mandar los parametros

        TransferenciaPropiaRequest transferenciaPropiaRequest = new TransferenciaPropiaRequest();
        transferenciaPropiaRequest.setAccountNumberOrigen("1230000003");
        transferenciaPropiaRequest.setAccountNumberDestino("1230000001");
        transferenciaPropiaRequest.setMonto(10.0);
        transferenciaPropiaRequest.setGlossa("Transferencia en test a cuenta propia");


        ErrorTransferencia tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaPropiaRequest)
                        .when().post("/users/tranferencias/propias").as(ErrorTransferencia.class);

        assertTrue(tranferenciasResponse.getEstado().equals("error"));
    }


    @Test
    public void postTransferenciaCuentasTerceros(){
        //DO verificar que las cuentas sean de terceros dentro del servicio

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
        //DO verificar que las cuentas sean de terceros dentro del servicio

        TransferenciaTerceroRequest transferenciaTerceroRequest = new TransferenciaTerceroRequest();
        transferenciaTerceroRequest.setAccountNumberOrigen("1230000003");
        transferenciaTerceroRequest.setAccountNumberDestino("1230000002");
        transferenciaTerceroRequest.setNombreDestinatario("Rooney Jacobs");
        transferenciaTerceroRequest.setMonto(10.0);
        transferenciaTerceroRequest.setGlossa("Transferencia en test a cuentas de terceros");

        ErrorTransferencia tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaTerceroRequest)
                        .when().post("/users/tranferencias/terceros").as(ErrorTransferencia.class);

        assertTrue(tranferenciasResponse.getEstado().equals("error"));

    }


    @Test
    public void postTransferenciaCuentasOtrosBancos(){
        //DO verificar que las cuentas sean de terceros dentro del servicio

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
        //DO verificar que las cuentas sean de otros bancos

        TransferenciaOtroBancoRequest transferenciaOtroBancoRequest = new TransferenciaOtroBancoRequest();
        transferenciaOtroBancoRequest.setAccountNumberOrigen("1230000003");
        transferenciaOtroBancoRequest.setAccountNumberDestino("1230000008");
        transferenciaOtroBancoRequest.setNombreDestinatario("Rooney Jacobs");
        transferenciaOtroBancoRequest.setNumeroBancoDestino("4");
        transferenciaOtroBancoRequest.setNombreBancoDestino("Banco Bisa");
        transferenciaOtroBancoRequest.setMonto(10.0);
        transferenciaOtroBancoRequest.setGlossa("Transferencia en test a cuentas para otros bancos");

        ErrorTransferencia tranferenciasResponse =
                given().contentType("application/json")
                        .body(transferenciaOtroBancoRequest)
                        .when().post("/users/tranferencias/terceros").as(ErrorTransferencia.class);

        assertTrue(tranferenciasResponse.getEstado().equals("error"));

    }

    @Test
    public void reversionTransferencia(){

        AutorizacionModel autorizacionReversion = given().pathParam("number", "AU001465")
                .when().get("/api/autorizacions/number/{number}")
                .then().extract().body().as(AutorizacionModel.class);
        autorizacionReversion.setAutorizacionId(2000008L);
        autorizacionReversion.setAutorizacionNumber("AU002000008");
        autorizacionReversion.setAutorizacionType("Reversion");
        autorizacionReversion.setEmpleadoNumber("E0003000008");

        AutorizacionModel autorizacionResponse= given()
                .contentType("application/json")
                .body(autorizacionReversion)
                .when().post("/api/autorizacions")
                .as(AutorizacionModel.class);

        ReversionRequest reversionRequest = new ReversionRequest();
        reversionRequest.setNumeroAutorizacion(autorizacionResponse.getAutorizacionNumber());
        reversionRequest.setNumeroTransacion("T0003000000007");

        TranferenciasResponse tranferenciasResponse =
                given().contentType("application/json")
                        .body(reversionRequest)
                        .when().post("/users/reversion/transferencia").as(TranferenciasResponse.class);

        assertTrue(tranferenciasResponse.getEstado().equals("successful"));


        AutorizacionModel autorizacion = new AutorizacionModel();
        autorizacion.setAutorizacionId(autorizacionResponse.getAutorizacionId());
        autorizacion.setAutorizacionNumber(autorizacionResponse.getAutorizacionNumber());

        given().
                contentType("application/json")
                .body(autorizacion)
                .when().delete("/api/autorizacions")
                .then().statusCode(200);

    }

}
