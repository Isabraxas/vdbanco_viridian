package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransaccionControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TransaccionControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getTransaccions() {
        given().when().get("/transaccions").then().statusCode(200);
    }

    @Test
    public void b_getTransaccionsPaginable() {
        given().when().get("/transaccions?page=1").then().statusCode(200);
    }

    @Test
    public void c_getTransaccionsById() {
        given().pathParam("id",189218)
                .when()
                    .get("/transaccions/{id}")
                    .then().statusCode(200)
                    .body("transaccionNumber", notNullValue());
        //Agregar algo mas del contenido del cuerpo como comprobar el transaccion number
    }

    @Test
    public void d_getTransaccionsByIdNotFound() {
        given()
                .pathParam("id",8000003)
        .when()
                .get("/transaccions/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("001"));
    }

    @Test
    public void e_getTransaccionsByNumber() {
        given().pathParam("number","T00019").when().get("/transaccions/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getTransaccionsByNumberNotFound() {
        given().pathParam("number","T0003000008").when().get("/transaccions/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postTransaccion(){
        TransaccionModel transaccion = new TransaccionModel();
        transaccion.setTransaccionId(8000003L);
        transaccion.setTransaccionNumber("T0008000003");
        transaccion.setTransaccionDate(new Timestamp(System.currentTimeMillis()));
        transaccion.setTransaccionMonto(0.0);
        transaccion.setAccountNumber("1230000019");
        transaccion.setAutorizacionNumber("AU0048");

        TransaccionModel transaccionResponse= given()
                .contentType("application/json")
                .body(transaccion)
                .when().post("/transaccions")
                .as(TransaccionModel.class);

        log.info("Response: "+ transaccionResponse.toString());

        assertTrue(transaccionResponse.getTransaccionNumber().equals("T0008000003"));
        assertTrue(transaccionResponse.getTransaccionMonto().equals(0.0));
        assertNotNull(transaccionResponse.getAccountNumber());

    }

    @Test
    public void h_putTransaccion(){

        TransaccionModel[] transaccion =
                given().pathParam("number","T0008000003").
                when().get("/transaccions/number/{number}")
                .then().extract().body().as(TransaccionModel[].class);
        transaccion[0].setTransaccionMonto(100.0);
        transaccion[0].setTransaccionGlossa("nuevoMonto");

        TransaccionModel transaccionResponse= given()
                .contentType("application/json")
                .body(transaccion[0])
                .when().put("/transaccions")
                .as(TransaccionModel.class);

        log.info("Response: "+ transaccionResponse.toString());

        assertTrue(transaccionResponse.getTransaccionMonto().equals(100.00));
        assertNotNull(transaccionResponse.getTransaccionGlossa());
        assertNotNull(transaccionResponse.getAccountNumber());
    }

    @Test
    public void i_deleteTransaccion(){
        TransaccionModel[] transaccion = given().pathParam("number","T0008000003").
                        when().get("/transaccions/number/{number}")
                        .then().extract().body().as(TransaccionModel[].class);

             given().
                contentType("application/json")
                .body(transaccion[0])
                .when().delete("/transaccions")
                .then().statusCode(200);
    }


}
