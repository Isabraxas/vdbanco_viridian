package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.EmpleadoModel;
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
public class EmpleadoControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(EmpleadoControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getEmpleados() {
        given().when().get("/empleados").then().statusCode(200);
    }

    @Test
    public void b_getEmpleadosPaginable() {
        given().when().get("/empleados?page=1").then().statusCode(200);
    }

    @Test
    public void c_getEmpleadosById() {
        given().pathParam("id",19)
                .when()
                    .get("/empleados/{id}")
                    .then().statusCode(200)
                    .body("personaPersonaNumber", notNullValue())
                    .body("empleadoNumber", notNullValue());
        //Agregar algo mas del contenido del cuerpo como comprobar el empleado number
    }

    @Test
    public void d_getEmpleadosByIdNotFound() {
        given()
                .pathParam("id",3000003)
        .when()
                .get("/empleados/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("001"));
    }

    @Test
    public void e_getEmpleadosByNumber() {
        given().pathParam("number","E00020").when().get("/empleados/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getEmpleadosByNumberNotFound() {
        given().pathParam("number","E0003000008").when().get("/empleados/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postEmpleado(){
        EmpleadoModel empleado = new EmpleadoModel();
        empleado.setEmpleadoId(3000003L);
        empleado.setEmpleadoNumber("E0003000003");
        empleado.setEmpleadoUsername("Sara12");
        empleado.setEmpleadoCargo("Secretaria");
        empleado.setEmpleadoGrupo("Administrativo");
        empleado.setEmpleadoAgencia("1");
        empleado.setPersonaPersonaNumber("P00021");

        EmpleadoModel empleadoResponse= given()
                .contentType("application/json")
                .body(empleado)
                .when().post("/empleados")
                .as(EmpleadoModel.class);

        log.info("Response: "+ empleadoResponse.toString());

        assertTrue(empleadoResponse.getPersonaPersonaNumber().equals("P00021"));
        assertTrue(empleadoResponse.getEmpleadoNumber().equals("E0003000003"));

    }

    @Test
    public void h_putEmpleado(){
        EmpleadoModel empleado = new EmpleadoModel();
        empleado.setEmpleadoId(3000003L);
        empleado.setEmpleadoNumber("E0003000003");
        empleado.setEmpleadoUsername("Sara21");
        empleado.setEmpleadoCargo("Secretaria");
        empleado.setEmpleadoGrupo("Administrativo");
        empleado.setEmpleadoAgencia("1");
        empleado.setPersonaPersonaNumber("P00021");

        EmpleadoModel empleadoResponse= given()
                .contentType("application/json")
                .body(empleado)
                .when().put("/empleados")
                .as(EmpleadoModel.class);

        log.info("Response: "+ empleadoResponse.toString());

        assertTrue(empleadoResponse.getEmpleadoUsername().equals("Sara21"));

    }

    @Test
    public void i_deleteEmpleado(){
        EmpleadoModel empleado = new EmpleadoModel();
        empleado.setEmpleadoId(3000003L);
        empleado.setEmpleadoNumber("P0003000003");

             given().
                contentType("application/json")
                .body(empleado)
                .when().delete("/empleados")
                .then().statusCode(200);
    }


}
