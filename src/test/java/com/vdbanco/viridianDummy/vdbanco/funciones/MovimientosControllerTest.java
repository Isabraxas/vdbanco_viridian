package com.vdbanco.viridianDummy.vdbanco.funciones;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.TransaccionModel;
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
import static org.hamcrest.Matchers.equalTo;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MovimientosControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MovimientosControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void getMovimientosByAccountTest(){

        TransaccionModel[] transaccions=
                given().pathParam("accountNumber", "1230000018")
                .when().get("/users/{accountNumber}/movimientos")
                .then().statusCode(200)
                .extract().as(TransaccionModel[].class);

        assertTrue(transaccions.length == 2);
    }

    @Test
    public void getLastMovimientosByAccountTest(){
        TransaccionModel[] transaccions=
                given().pathParam("accountNumber", "1230000001")
                .when().get("/users/{accountNumber}/movimientos/top")
                .then().statusCode(200)
                .extract().as(TransaccionModel[].class);

        assertTrue(transaccions.length == 10);

    }


    @Test
    public void getMovimientosByAccountAndLastMonthsTest(){
        TransaccionModel[] transaccions=
                given().pathParam("accountNumber", "1230000001")
                        .pathParam("numberMonth","0")
                        .when().get("/users/{accountNumber}/movimientos/month/{numberMonth}")
                        .then().statusCode(200)
                        .extract().as(TransaccionModel[].class);

        assertTrue(transaccions.length == 3);

    }

    @Test
    public void getMovimientosByAccountAndFechasTest(){
        TransaccionModel[] transaccions=
                given().pathParam("accountNumber", "1230000001")
                        .pathParam("fechaDesde","2018-04-19 01:55:23")
                        .pathParam("fechaHasta","2018-05-19 23:55:23")
                        .when().get("/users/{accountNumber}/movimientos/desde/{fechaDesde}/hasta/{fechaHasta}")
                        .then().statusCode(200)
                        .extract().as(TransaccionModel[].class);

        assertTrue(transaccions.length == 13);

    }

    @Test
    public void getMovimientosByAccountAndFechasPTest(){
        TransaccionModel[] transaccions=
                given().pathParam("accountNumber", "1230000001")
                        .queryParam("fechaDesde","2018-04-19")
                        .queryParam("fechaHasta","2018-05-19")
                        .when().get("/users/{accountNumber}/movimientosP")
                        .then().statusCode(200)
                        .extract().as(TransaccionModel[].class);

        assertTrue(transaccions.length == 13);

    }
}
