package com.vdbanco.viridianDummy.funciones;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class ProductosClienteController {

    private ProductosClienteService productosClienteService;

    @Autowired
    public ProductosClienteController(ProductosClienteService productosClienteService) {
        this.productosClienteService = productosClienteService;
    }

    @GetMapping(value = "/{id}/productos")
    public ProductosClienteModel getProductosByUser(@PathVariable Long id){
        return this.productosClienteService.getProductosByUser(id);
    }
}
