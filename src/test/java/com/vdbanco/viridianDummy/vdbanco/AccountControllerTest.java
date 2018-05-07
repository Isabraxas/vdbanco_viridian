package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.AccountModel;
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
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AccountControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getAccounts() {
        given().when().get("/accounts").then().statusCode(200);
    }

    @Test
    public void b_getAccountsPaginable() {
        given().when().get("/accounts?page=1").then().statusCode(200);
    }

    @Test
    public void c_getAccountsById() {
        given().pathParam("id",10).when().get("/accounts/{id}").then().statusCode(200);
    }

    @Test
    public void d_getAccountsByIdNotFound() {
        given()
                .pathParam("id",3000003)
        .when()
                .get("/accounts/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("001"));
    }

    @Test
    public void e_getAccountsByNumber() {
        given().pathParam("number","1230000001").when().get("/accounts/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getAccountsByNumberNotFound() {
        given().pathParam("number","1230000040").when().get("/accounts/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postAccount(){
        Timestamp fechaApertura = new Timestamp(System.currentTimeMillis());
        AccountModel account = new AccountModel();
        account.setAccountId(31L);
        account.setAccountTipo("credito");
        account.setAccountBalance(100.00);
        account.setAccountFechaApertura(fechaApertura);
        account.setAccountHolderNumber("H0001");
        account.setProductosBancariosNumber("B0009");

        AccountModel accountResponse= given()
                .contentType("application/json")
                .body(account)
                .when().post("/accounts")
                .as(AccountModel.class);

        log.info("Response: "+ accountResponse.toString());

        assertTrue(accountResponse.getProductosBancariosNumber().equals("B0009"));
        assertTrue(accountResponse.getAccountTipo().equals("credito"));

    }

    @Test
    public void h_putAccount(){
        Timestamp fechaApertura = new Timestamp(System.currentTimeMillis());
        AccountModel account = new AccountModel();
        account.setAccountId(31L);
        account.setAccountNumber("1230000031");
        account.setAccountTipo("credito");
        account.setAccountBalance(120.00);
        account.setAccountFechaApertura(fechaApertura);
        account.setAccountHolderNumber("H0002");
        account.setProductosBancariosNumber("B0009");

        AccountModel accountResponse= given()
                .contentType("application/json")
                .body(account)
                .when().put("/accounts")
                .as(AccountModel.class);

        log.info("Response: "+ accountResponse.toString());

        assertTrue(accountResponse.getAccountBalance().equals(120.00));

    }

    @Test
    public void i_deleteAccount(){
        AccountModel account = new AccountModel();
        account.setAccountId(31L);
        account.setAccountNumber("1230000031");

             given().
                contentType("application/json")
                .body(account)
                .when().delete("/accounts")
                .then().statusCode(200);
    }


}
