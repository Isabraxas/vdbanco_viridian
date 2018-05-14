package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.UserModel;
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

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getUsers() {
        given().when().get("/api/users").then().statusCode(200);
    }

    @Test
    public void b_getUsersPaginable() {
        given().when().get("/api/users?page=1").then().statusCode(200);
    }

    @Test
    public void c_getUsersById() {
        given().pathParam("id",20)
                .when()
                    .get("/api/users/{id}")
                    .then().statusCode(200);
        //Agregar algo mas del contenido del cuerpo como comprobar el user number
    }

    @Test
    public void d_getUsersByIdNotFound() {
        given()
                .pathParam("id",3000003)
        .when()
                .get("/api/users/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("001"));
    }

    @Test
    public void e_getUsersByNumber() {
        given().pathParam("number","U00020").when().get("/api/users/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getUsersByNumberNotFound() {
        given().pathParam("number","U0003000008").when().get("/api/users/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postUser(){
        UserModel user = new UserModel();
        user.setUserId(3000003L);
        user.setUserNumber("U0003000003");
        user.setUserCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setUserName("Sara123");
        user.setUserPassword("123456");
        user.setPersonaPersonaNumber("P00021");

        UserModel userResponse= given()
                .contentType("application/json")
                .body(user)
                .when().post("/api/users")
                .as(UserModel.class);

        log.info("Response: "+ userResponse.toString());

        assertTrue(userResponse.getPersonaPersonaNumber().equals("P00021"));
        assertTrue(userResponse.getUserNumber().equals("U0003000003"));

    }

    @Test
    public void h_putUser(){
        UserModel user = new UserModel();
        user.setUserId(3000003L);
        user.setUserNumber("U0003000003");
        user.setUserCreateTime(new Timestamp(System.currentTimeMillis()));
        user.setUserName("Sara21");
        user.setUserPassword("123456");
        user.setPersonaPersonaNumber("P00021");

        UserModel userResponse= given()
                .contentType("application/json")
                .body(user)
                .when().put("/api/users")
                .as(UserModel.class);

        log.info("Response: "+ userResponse.toString());

        assertTrue(userResponse.getUserName().equals("Sara21"));

    }

    @Test
    public void i_deleteUser(){
        UserModel user = new UserModel();
        user.setUserId(3000003L);
        user.setUserNumber("U0003000003");

             given().
                contentType("application/json")
                .body(user)
                .when().delete("/api/users")
                .then().statusCode(200);
    }


}
