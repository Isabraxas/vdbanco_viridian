package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.AccountHolderModel;
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
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountHolderControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountHolderControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getAccountHolders() {
        given().when().get("/api/accountHolders").then().statusCode(200);
    }

    @Test
    public void b_getAccountHoldersPaginable() {
        given().when().get("/api/accountHolders?page=1").then().statusCode(200);
    }

    @Test
    public void c_getAccountHoldersById() {
        given().pathParam("id",5).when().get("/api/accountHolders/{id}").then().statusCode(200);
    }

    @Test
    public void d_getAccountHoldersByIdNotFound() {
        given()
                .pathParam("id",31)
        .when()
                .get("/api/accountHolders/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("404"));
    }

    @Test
    public void e_getAccountHoldersByNumber() {
        given().pathParam("number","H0005").when().get("/api/accountHolders/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getAccountHoldersByNumberNotFound() {
        given().pathParam("number","H00031").when().get("/api/accountHolders/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postAccountHolder(){
        AccountHolderModel accountHolder = new AccountHolderModel();
        accountHolder.setAccountHolderId(31L);
        accountHolder.setAccountHolderNumber("H00031");
        accountHolder.setAccountHolderTipo("J");
        accountHolder.setAccountHolderTitularNumber("P00055");
        accountHolder.setAccountHolderApoderadoNumber("P00020");
        accountHolder.setJuridicasJuridicasNumber("J00016");

        AccountHolderModel accountHolderResponse= given()
                .contentType("application/json")
                .body(accountHolder)
                .when().post("/api/accountHolders")
                .as(AccountHolderModel.class);

        log.info("Response: "+ accountHolderResponse.toString());

        assertTrue(accountHolderResponse.getAccountHolderTipo().equals("J"));
        assertTrue(accountHolderResponse.getJuridicasJuridicasNumber().equals("J00016"));
        assertNull(accountHolderResponse.getPersonaPersonaNumber());
    }

    @Test
    public void h_putAccountHolder(){
        AccountHolderModel accountHolder = new AccountHolderModel();
        accountHolder.setAccountHolderId(31L);
        accountHolder.setAccountHolderNumber("H00031");
        accountHolder.setAccountHolderTipo("N");
        accountHolder.setAccountHolderTitularNumber("P00055");
        accountHolder.setAccountHolderApoderadoNumber("P00020");
        accountHolder.setPersonaPersonaNumber("P00068");

        AccountHolderModel accountHolderResponse= given()
                .contentType("application/json")
                .body(accountHolder)
                .when().put("/api/accountHolders")
                .as(AccountHolderModel.class);

        log.info("Response: "+ accountHolderResponse.toString());


        assertTrue(accountHolderResponse.getAccountHolderTipo().equals("N"));
        assertTrue(accountHolderResponse.getPersonaPersonaNumber().equals("P00068"));
        assertNull(accountHolderResponse.getJuridicasJuridicasNumber());

    }

    @Test
    public void i_deleteAccountHolder(){
        AccountHolderModel accountHolder = new AccountHolderModel();
        accountHolder.setAccountHolderId(31L);
        accountHolder.setAccountHolderNumber("H00031");

             given().
                contentType("application/json")
                .body(accountHolder)
                .when().delete("/api/accountHolders")
                .then().statusCode(204);
    }


}
