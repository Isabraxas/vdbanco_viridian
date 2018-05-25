package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.AutorizacionModel;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AutorizacionControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AutorizacionControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getAutorizacions() {
        given().when().get("/api/autorizacions").then().statusCode(200);
    }

    @Test
    public void b_getAutorizacionsPaginable() {
        given().when().get("/api/autorizacions?page=1").then().statusCode(200);
    }

    @Test
    public void c_getAutorizacionsById() {
        given().pathParam("id",1466)
                .when()
                    .get("/api/autorizacions/{id}")
                    .then().statusCode(200)
                    .body("empleadoNumber", notNullValue())
                    .body("autorizacionNumber", notNullValue());
        //Agregar algo mas del contenido del cuerpo como comprobar el autorizacion number
    }

    @Test
    public void d_getAutorizacionsByIdNotFound() {
        given()
                .pathParam("id",3000003)
        .when()
                .get("/api/autorizacions/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("404"));
    }

    @Test
    public void e_getAutorizacionsByNumber() {
        given().pathParam("number","AU001447").when().get("/api/autorizacions/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getAutorizacionsByNumberNotFound() {
        given().pathParam("number","AU003000008").when().get("/api/autorizacions/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postAutorizacion(){
        AutorizacionModel autorizacion = given().pathParam("id", "1447")
                .when().get("/api/autorizacions/{id}")
                .then().extract().body().as(AutorizacionModel.class);
        autorizacion.setAutorizacionId(3000008L);
        autorizacion.setAutorizacionNumber("AU003000008");
        autorizacion.setEmpleadoNumber("E0003000008");

        AutorizacionModel autorizacionResponse= given()
                .contentType("application/json")
                .body(autorizacion)
                .when().post("/api/autorizacions")
                .as(AutorizacionModel.class);

        log.info("Response: "+ autorizacionResponse.toString());

        assertTrue(autorizacionResponse.getEmpleadoNumber().equals("E0003000008"));
        assertTrue(autorizacionResponse.getAutorizacionNumber().equals("AU003000008"));

    }

    @Test
    public void h_putAutorizacion(){
        AutorizacionModel autorizacion = given().pathParam("number", "AU003000008")
                .when().get("/api/autorizacions/number/{number}")
                .then().extract().body().as(AutorizacionModel.class);
        autorizacion.setAutorizacionGlossa("Para borrar");


        AutorizacionModel autorizacionResponse= given()
                .contentType("application/json")
                .body(autorizacion)
                .when().put("/api/autorizacions")
                .as(AutorizacionModel.class);

        log.info("Response: "+ autorizacionResponse.toString());

        assertTrue(autorizacionResponse.getAutorizacionGlossa().equals("Para borrar"));

    }

    @Test
    public void i_deleteAutorizacion(){
        AutorizacionModel autorizacion = new AutorizacionModel();
        autorizacion.setAutorizacionId(3000008L);
        autorizacion.setAutorizacionNumber("AU003000008");

             given().
                contentType("application/json")
                .body(autorizacion)
                .when().delete("/api/autorizacions")
                .then().statusCode(204);
    }


}
