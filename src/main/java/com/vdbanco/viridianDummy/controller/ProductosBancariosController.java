package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.ProductosBancariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/productosBancarios")
public class ProductosBancariosController {

    private ProductosBancariosService productosBancariosService;

    @Autowired
    public ProductosBancariosController(ProductosBancariosService productosBancariosService) {
        this.productosBancariosService = productosBancariosService;
    }

    @GetMapping
    public Page<ProductosBancariosModel> getAllPageable(Pageable pageable){
        return this.productosBancariosService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<ProductosBancariosModel> getProductosBancariosById(@PathVariable Long id){
        return this.productosBancariosService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public ProductosBancariosModel getProductosBancariosByNumber(@PathVariable String number){
        return this.productosBancariosService.getByProductosBancariosNumber(number);
    }

    @PostMapping
    public ProductosBancariosModel saveProductosBancarios(@RequestBody ProductosBancariosModel productosBancarios){
        return this.productosBancariosService.save(productosBancarios);
    }

    @PutMapping
    public ProductosBancariosModel updateProductosBancarios(@RequestBody ProductosBancariosModel productosBancarios){

        return this.productosBancariosService.update(productosBancarios);
    }

    @DeleteMapping
    public void deleteProductosBancarios(@RequestBody ProductosBancariosModel productosBancarios){
        this.productosBancariosService.delete(productosBancarios);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoEncontradoRestException.class)
    public EntidadError handleNotFound(NoEncontradoRestException exception){
        EntidadError error = new EntidadError();
        error.setId(exception.getErrorNoEncontrado().getId());
        error.setEstado("error");
        error.setError(exception.getErrorNoEncontrado());
        return error;
    }

}
