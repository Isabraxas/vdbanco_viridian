package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.FuncionalTest;
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
@FixMethodOrder(MethodSorters.JVM)
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
    public void getPersonas() {
        given().when().get("/personas").then().statusCode(200);
    }

    @Test
    public void getPersonasPaginable() {
        given().when().get("/personas?page=1").then().statusCode(200);
    }

    @Test
    public void getPersonasById() {
        given().pathParam("id",120).when().get("/personas/{id}").then().statusCode(200);
    }

    @Test
    public void getPersonasByIdNotFound() {
        given()
                .pathParam("id",3000003)
        .when()
                .get("/personas/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("001"));
    }

    @Test
    public void getPersonasByNumber() {
        given().pathParam("number","P000120").when().get("/personas/number/{number}").then().statusCode(200);
    }

    @Test
    public void getPersonasByNumberNotFound() {
        given().pathParam("number","P0003000008").when().get("/personas/number/{number}").then().statusCode(404);
    }

    @Test
    public void postPersona(){
        PersonaModel persona = new PersonaModel();
        persona.setPersonaId(3000003L);
        persona.setPersonaNombre("In");
        persona.setPersonaNumber("P0003000003");
        persona.setPersonaApellidoCazada("algo");

        PersonaModel personaResponse= given()
                .contentType("application/json")
                .body(persona)
                .when().post("/personas")
                .as(PersonaModel.class);

        log.info("Response: "+ personaResponse.toString());

        assertTrue(personaResponse.getPersonaNombre().equals("In"));
        assertTrue(personaResponse.getPersonaApellidoCazada().equals("algo"));

    }

    @Test
    public void putPersona(){
        PersonaModel persona = new PersonaModel();
        persona.setPersonaId(3000003L);
        persona.setPersonaNumber("P0003000003");
        persona.setPersonaApellidoCazada("ninguno");

        PersonaModel personaResponse= given()
                .contentType("application/json")
                .body(persona)
                .when().put("/personas")
                .as(PersonaModel.class);

        log.info("Response: "+ personaResponse.toString());

        assertTrue(personaResponse.getPersonaApellidoCazada().equals("ninguno"));

    }

    @Test
    public void deletePersona(){
        PersonaModel persona = new PersonaModel();
        persona.setPersonaId(3000003L);
        persona.setPersonaNumber("P0003000003");

             given().
                contentType("application/json")
                .body(persona)
                .when().delete("/personas")
                .then().statusCode(200);
    }


}
