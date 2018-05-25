package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.JuridicasModel;
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
public class JuridicasControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JuridicasControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getJuridicass() {
        given().when().get("/api/juridicas").then().statusCode(200);
    }

    @Test
    public void b_getJuridicassPaginable() {
        given().when().get("/api/juridicas?page=1").then().statusCode(200);
    }

    @Test
    public void c_getJuridicassById() {
        given().pathParam("id",8)
                .when()
                    .get("/api/juridicas/{id}")
                    .then().statusCode(200)
                    .body("juridicasRepresentanteLegalNumber", notNullValue())
                    .body("juridicasNumber", notNullValue());
        //Agregar algo mas del contenido del cuerpo como comprobar el juridica number
    }

    @Test
    public void d_getJuridicassByIdNotFound() {
        given()
                .pathParam("id",3000003)
        .when()
                .get("/api/juridicas/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("404"));
    }

    @Test
    public void e_getJuridicassByNumber() {
        given().pathParam("number","J0008").when().get("/api/juridicas/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getJuridicassByNumberNotFound() {
        given().pathParam("number","J0003000003").when().get("/api/juridicas/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postJuridicas(){
        JuridicasModel juridica = new JuridicasModel();
        juridica.setJuridicasId(3000003L);
        juridica.setJuridicasNumber("J0003000003");
        juridica.setJuridicasNit("78554466541");
        juridica.setJuridicasRepresentanteLegalNumber("P00044");

        JuridicasModel juridicaResponse= given()
                .contentType("application/json")
                .body(juridica)
                .when().post("/api/juridicas")
                .as(JuridicasModel.class);

        log.info("Response: "+ juridicaResponse.toString());

        assertTrue(juridicaResponse.getJuridicasRepresentanteLegalNumber().equals("P00044"));
        assertTrue(juridicaResponse.getJuridicasNumber().equals("J0003000003"));

    }

    @Test
    public void h_putJuridicas(){
        JuridicasModel juridica = new JuridicasModel();
        juridica.setJuridicasId(3000003L);
        juridica.setJuridicasNumber("J0003000003");
        juridica.setJuridicasNit("78555466541");
        juridica.setJuridicasRepresentanteLegalNumber("P00044");

        JuridicasModel juridicaResponse= given()
                .contentType("application/json")
                .body(juridica)
                .when().put("/api/juridicas")
                .as(JuridicasModel.class);

        log.info("Response: "+ juridicaResponse.toString());

        assertTrue(juridicaResponse.getJuridicasNit().equals("78555466541"));

    }

    @Test
    public void i_deleteJuridicas(){
        JuridicasModel juridica = new JuridicasModel();
        juridica.setJuridicasId(3000003L);
        juridica.setJuridicasNumber("J0003000003");

             given().
                contentType("application/json")
                .body(juridica)
                .when().delete("/api/juridicas")
                .then().statusCode(204);
    }


}
