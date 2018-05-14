package com.vdbanco.viridianDummy.vdbanco;

import com.jayway.restassured.RestAssured;
import com.vdbanco.viridianDummy.ViridianDummyApplication;
import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ViridianDummyApplication.class , webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductosBancariosControllerTest {
    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ProductosBancariosControllerTest.class);

    @Value("${local.server.port}")
    private int serverPort;

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
    }

    @Test
    public void a_getProductosBancarios() {
        given().when().get("/api/productosBancarios").then().statusCode(200);
    }

    @Test
    public void b_getProductosBancariosPaginable() {
        given().when().get("/api/productosBancarios?page=1").then().statusCode(200);
    }

    @Test
    public void c_getProductosBancariosById() {
        given().pathParam("id",5).when().get("/api/productosBancarios/{id}").then().statusCode(200);
    }

    @Test
    public void d_getProductosBancariosByIdNotFound() {
        given()
                .pathParam("id",31)
        .when()
                .get("/api/productosBancarios/{id}").then().statusCode(404).and()
                .body("estado",equalTo("error"))
                .body("error.codigo",equalTo("001"));
    }

    @Test
    public void e_getProductosBancariosByNumber() {
        given().pathParam("number","B0005").when().get("/api/productosBancarios/number/{number}").then().statusCode(200);
    }

    @Test
    public void f_getProductosBancariosByNumberNotFound() {
        given().pathParam("number","B00031").when().get("/api/productosBancarios/number/{number}").then().statusCode(404);
    }

    @Test
    public void g_postProductosBancarios(){
        ProductosBancariosModel productosBancarios = new ProductosBancariosModel();
        productosBancarios.setProductosBancariosId(31L);
        productosBancarios.setProductosBancariosNumber("B00031");
        productosBancarios.setProductosBancariosNombre("Cuenta nomina");

        ProductosBancariosModel productosBancariosResponse= given()
                .contentType("application/json")
                .body(productosBancarios)
                .when().post("/api/productosBancarios")
                .as(ProductosBancariosModel.class);

        log.info("Response: "+ productosBancariosResponse.toString());

        assertTrue(productosBancariosResponse.getProductosBancariosNombre().equals("Cuenta nomina"));
        //assertNotNull(productosBancariosResponse.getProductosBancariosNumber());
    }

    @Test
    public void h_putProductosBancarios(){
        ProductosBancariosModel productosBancarios = new ProductosBancariosModel();
        productosBancarios.setProductosBancariosId(31L);
        productosBancarios.setProductosBancariosNumber("B00031");
        productosBancarios.setProductosBancariosNombre("Cuenta de valores");

        ProductosBancariosModel productosBancariosResponse= given()
                .contentType("application/json")
                .body(productosBancarios)
                .when().put("/api/productosBancarios")
                .as(ProductosBancariosModel.class);

        log.info("Response: "+ productosBancariosResponse.toString());


        assertTrue(productosBancariosResponse.getProductosBancariosNombre().equals("Cuenta de valores"));
        assertNotNull(productosBancariosResponse.getProductosBancariosNumber());

    }

    @Test
    public void i_deleteProductosBancarios(){
        ProductosBancariosModel productosBancarios = new ProductosBancariosModel();
        productosBancarios.setProductosBancariosId(31L);
        productosBancarios.setProductosBancariosNumber("B00031");

             given().
                contentType("application/json")
                .body(productosBancarios)
                .when().delete("/api/productosBancarios")
                .then().statusCode(200);
    }


}
