package com.vdbanco.viridianDummy.funciones;


import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
@Api(
        name = "Productos banacarios usuario",
        description = "Permite consultar por medio del usuario el detalle de datos de la persona asociada y sus cuentas.",
        stage = ApiStage.ALPHA
)
public class ProductosClienteController {

    private ProductosClienteService productosClienteService;

    @Autowired
    public ProductosClienteController(ProductosClienteService productosClienteService) {
        this.productosClienteService = productosClienteService;
    }

    @GetMapping(value = "/{username}/productos")
    @ApiMethod(description = "Retorna los datos personales y las cuentas asociadas al usuario")
    public ProductosClienteModel getProductosByUser(@ApiPathParam(name="username") @PathVariable String username){
        return this.productosClienteService.getProductosByUser(username);
    }

}
