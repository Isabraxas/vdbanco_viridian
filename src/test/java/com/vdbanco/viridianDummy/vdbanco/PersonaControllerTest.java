package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.PersonaModel;
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
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PersonaControllerTest  {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PersonaControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getPersonas() {
        given().when().get("/api/personas").then().statusCode(200);
    }

    @Test
    public void b_getPersonasPaginable() {
        given().when().get("/api/personas?page=1").then().statusCode(200);
    }

    @Test
    public void c_getPersonasById() {
        given().pathParam("id",20).when().get("/api/personas/{id}").then().statusCode(200);
    }

    @Test
    public void d_getPersonasByIdNotFound() {
        given()
                .pathParam("id",3000003)
        .when()
                .get("/api/personas/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("001"));
    }

    @Test
    public void e_getPersonasByNumber() {
        given().pathParam("number","P00012").when().get("/api/personas/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getPersonasByNumberNotFound() {
        given().pathParam("number","P0003000008").when().get("/api/personas/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postPersona(){
        PersonaModel persona = new PersonaModel();
        persona.setPersonaId(3000003L);
        persona.setPersonaNombre("In");
        persona.setPersonaNumber("P0003000003");
        persona.setPersonaApellidoCazada("algo");

        PersonaModel personaResponse= given()
                .contentType("application/json")
                .body(persona)
                .when().post("/api/personas")
                .as(PersonaModel.class);

        log.info("Response: "+ personaResponse.toString());

        assertTrue(personaResponse.getPersonaNombre().equals("In"));
        assertTrue(personaResponse.getPersonaApellidoCazada().equals("algo"));

    }

    @Test
    public void h_putPersona(){
        PersonaModel persona = new PersonaModel();
        persona.setPersonaId(3000003L);
        persona.setPersonaNumber("P0003000003");
        persona.setPersonaApellidoCazada("ninguno");

        PersonaModel personaResponse= given()
                .contentType("application/json")
                .body(persona)
                .when().put("/api/personas")
                .as(PersonaModel.class);

        log.info("Response: "+ personaResponse.toString());

        assertTrue(personaResponse.getPersonaApellidoCazada().equals("ninguno"));

    }

    @Test
    public void i_deletePersona(){
        PersonaModel persona = new PersonaModel();
        persona.setPersonaId(3000003L);
        persona.setPersonaNumber("P0003000003");

             given().
                contentType("application/json")
                .body(persona)
                .when().delete("/api/personas")
                .then().statusCode(200);
    }


}
