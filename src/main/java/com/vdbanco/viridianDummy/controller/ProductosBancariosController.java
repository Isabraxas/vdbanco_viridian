package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
import com.vdbanco.viridianDummy.domain.ProductosBancariosModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.ProductosBancariosService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiQueryParam;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/productosBancarios")
@Api(
        name = "Productos bancarios",
        description = "Permite manejar a travez de una lista de metodos los productos bancarios del banco.",
        stage = ApiStage.ALPHA
)
public class ProductosBancariosController {

    private ProductosBancariosService productosBancariosService;
    private Environment env;

    @Autowired
    public ProductosBancariosController(ProductosBancariosService productosBancariosService, Environment env) {
        this.productosBancariosService = productosBancariosService;
        this.env = env;
    }

    @GetMapping
    @ApiMethod(description = "Retorna una lista de productos bancarios paginados de 20 en 20")
    public ProductosBancariosModelList getAllPageable(@ApiQueryParam(name = "page", description = "numero de pagina", required = false, defaultvalue = "0")
                                                          @RequestParam( required = false, defaultValue = "0") int page,
                                                      @ApiQueryParam(name = "size", description = "numero de pagina", required = false, defaultvalue = "20")
                                                            @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<ProductosBancariosModel> productosBancariosPages = this.productosBancariosService.getAll(pageRequest);
        List<ProductosBancariosModel> productosBancariosList = productosBancariosPages.getContent();

        for (ProductosBancariosModel productosBancarios: productosBancariosList ) {
            productosBancarios.add(linkTo(methodOn(ProductosBancariosController.class).getProductosBancariosByNumber(productosBancarios.getProductosBancariosNumber())).withSelfRel());
        }

        ProductosBancariosModelList productosBancariosModelList = new ProductosBancariosModelList(productosBancariosList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(ProductosBancariosController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(ProductosBancariosController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        productosBancariosModelList.add(linkNext);
        productosBancariosModelList.add(linkPrevious);
        return productosBancariosModelList;

    }

    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Retorna el producto bancario correspondiente con id proporcionado en el path.")
    public Optional<ProductosBancariosModel> getProductosBancariosById(@ApiPathParam(name="id",description = "id de producto bancario") @PathVariable Long id){
        return this.productosBancariosService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    @ApiMethod(description = "Retorna el producto bancario correspondiente con el numero/codigo proporcionado en el path.")
    public ProductosBancariosModel getProductosBancariosByNumber(@ApiPathParam(name = "number" , description = "numero o codigo de producto bancario") @PathVariable String number){

        ProductosBancariosModel productosBancarios = this.productosBancariosService.getByProductosBancariosNumber(number);
        productosBancarios.add(linkTo(methodOn(ProductosBancariosController.class).getProductosBancariosByNumber(productosBancarios.getProductosBancariosNumber())).withSelfRel());
        productosBancarios.add(linkTo(methodOn(ProductosBancariosController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de productosBancarios"));
        return productosBancarios;
    }
    @PostMapping
    @ApiMethod(description = "Almacena un nuevo producto bancario con los datos ingresados.", responsestatuscode = "201" )
    public ProductosBancariosModel saveProductosBancarios(@RequestBody @Valid ProductosBancariosModel productosBancarios){
        return this.productosBancariosService.save(productosBancarios);
    }

    @PutMapping
    @ApiMethod(description = "Sustituye los datos del producto bancario que corresponda con el mismo numero/codigo")
    public ProductosBancariosModel updateProductosBancarios(@RequestBody @Valid ProductosBancariosModel productosBancarios){

        return this.productosBancariosService.update(productosBancarios);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping
    @ApiMethod(description = "Elimina el producto bancario que corresponda con el mismo numero/codigo")
    public void deleteProductosBancarios(@RequestBody ProductosBancariosModel productosBancarios){
        this.productosBancariosService.delete(productosBancarios);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoEncontradoRestException.class)
    public EntidadError handleNotFound(NoEncontradoRestException exception){
        EntidadError error = new EntidadError();
        error.setId(exception.getErrorDetalle().getId());
        error.setEstado("error");
        error.setError(exception.getErrorDetalle());
        return error;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictsException.class)
    public EntidadError handleConflict(ConflictsException exception){
        EntidadError error = new EntidadError();
        error.setId(exception.getErrorDetalle().getId());
        error.setEstado("error");
        error.setError(exception.getErrorDetalle());
        return error;
    }

}
