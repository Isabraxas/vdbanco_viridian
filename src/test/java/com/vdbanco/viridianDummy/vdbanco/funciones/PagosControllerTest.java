package com.vdbanco.viridianDummy.vdbanco.funciones;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.error.ErrorTransferencia;
import com.vdbanco.viridianDummy.funciones.inputModel.PagoPrestamoRequest;
import com.vdbanco.viridianDummy.funciones.outputModel.PagoResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PagosControllerTest {

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }


    @Test
    public void postPagoDeServicio(){

    }

    @Test
    public void postPagoPrestamo(){
        PagoPrestamoRequest pagoPrestamoRequest = new PagoPrestamoRequest();
        pagoPrestamoRequest.setAccountNumberOrigen("1230000004");
        pagoPrestamoRequest.setAccountNumberDestino("1230000027");
        pagoPrestamoRequest.setMonto(5.0);
        pagoPrestamoRequest.setGlossa("Pago de prestamo test");

        PagoResponse pagoResponse =
                given().contentType("application/json")
                        .body(pagoPrestamoRequest)
                .when().post("/users/pagos/prestamo")
                        .as(PagoResponse.class);

        Assert.assertTrue(pagoResponse.getEstado().equals("successful"));

    }

    @Test
    public void postPagoPrestamoNotWork(){
        PagoPrestamoRequest pagoPrestamoRequest = new PagoPrestamoRequest();
        pagoPrestamoRequest.setAccountNumberOrigen("1230000004");
        pagoPrestamoRequest.setAccountNumberDestino("1230000016");
        pagoPrestamoRequest.setMonto(5.0);
        pagoPrestamoRequest.setGlossa("Pago de prestamo test");

        ErrorTransferencia pagoResponse =
                given().contentType("application/json")
                        .body(pagoPrestamoRequest)
                        .when().post("/users/pagos/prestamo")
                        .as(ErrorTransferencia.class);

        Assert.assertTrue(pagoResponse.getEstado().equals("error"));

    }



    @Test
    public  void postPagoTarjetaCredito(){

        PagoPrestamoRequest pagoPrestamoRequest = new PagoPrestamoRequest();
        pagoPrestamoRequest.setAccountNumberOrigen("1230000004");
        pagoPrestamoRequest.setAccountNumberDestino("1230000029");
        pagoPrestamoRequest.setMonto(5.0);
        pagoPrestamoRequest.setGlossa("Pago de tarjeta de credito");

        PagoResponse pagoResponse =
                given().contentType("application/json")
                        .body(pagoPrestamoRequest)
                        .when().post("/users/pagos/tarjetaCredito")
                        .as(PagoResponse.class);

        Assert.assertTrue(pagoResponse.getEstado().equals("successful"));
        Assert.assertTrue(pagoResponse.getDetalle().getTransaccionGlossa().equals("Pago de tarjeta de credito"));
    }

    @Test
    public  void postPagoTarjetaCreditoNotWork(){

        PagoPrestamoRequest pagoPrestamoRequest = new PagoPrestamoRequest();
        pagoPrestamoRequest.setAccountNumberOrigen("1230000004");
        pagoPrestamoRequest.setAccountNumberDestino("1230000027");
        pagoPrestamoRequest.setMonto(5.0);
        pagoPrestamoRequest.setGlossa("Pago de tarjeta de credito");

        ErrorTransferencia pagoResponse =
                given().contentType("application/json")
                        .body(pagoPrestamoRequest)
                        .when().post("/users/pagos/tarjetaCredito")
                        .as(ErrorTransferencia.class);

        Assert.assertTrue(pagoResponse.getEstado().equals("error"));
    }

}
