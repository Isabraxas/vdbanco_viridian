package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.EmpleadoModel;
import com.vdbanco.viridianDummy.domain.EmpleadoModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.EmpleadoService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
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
@RequestMapping(value = "/api/empleados")
@Api(
        name = "Empleados",
        description = "Permite manejar por medio de una lista de metodos las operaciones aplicadas a los empleados del banco.",
        stage = ApiStage.ALPHA
)
public class EmpleadoController {

    private EmpleadoService empleadoService;
    private Environment env;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService, Environment env) {
        this.empleadoService = empleadoService;
        this.env = env;
    }

    @GetMapping
    @ApiMethod(description = "Retorna una lista de empleados paginados de 20 en 20")
    public EmpleadoModelList getAllPageable(@RequestParam( required = false, defaultValue = "0") int page,
                                           @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<EmpleadoModel> empleadoPages = this.empleadoService.getAll(pageRequest);
        List<EmpleadoModel> empleadoList = empleadoPages.getContent();

        for (EmpleadoModel empleado: empleadoList ) {
            empleado.add(linkTo(methodOn(EmpleadoController.class).getEmpleadoByNumber(empleado.getEmpleadoNumber())).withSelfRel());
        }

        EmpleadoModelList empleadoModelList = new EmpleadoModelList(empleadoList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(EmpleadoController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(EmpleadoController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        empleadoModelList.add(linkNext);
        empleadoModelList.add(linkPrevious);
        return empleadoModelList;

    }

    @GetMapping(value = "/{id}")
    @ApiMethod(description = "Retorna el empleado correspondiente con id proporcionado en el path.")
    public Optional<EmpleadoModel> getEmpleadoById(@ApiPathParam(name="id",description = "id de empleado") @PathVariable Long id){
        return this.empleadoService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    @ApiMethod(description = "Retorna el empleado correspondiente con el numero/codigo proporcionado en el path.")
    public EmpleadoModel getEmpleadoByNumber(@ApiPathParam(name = "number" , description = "numero o codigo de empleado")@PathVariable String number){

        EmpleadoModel empleado = this.empleadoService.getByEmpleadoNumber(number);
        empleado.add(linkTo(methodOn(EmpleadoController.class).getEmpleadoByNumber(empleado.getEmpleadoNumber())).withSelfRel());
        empleado.add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(empleado.getPersonaPersonaNumber())).withRel("persona"));
        empleado.add(linkTo(methodOn(EmpleadoController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de empleados"));
        return empleado;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    @ApiMethod(description = "Almacena un nuevo empleado con los datos ingresados.", responsestatuscode = "201" )
    public EmpleadoModel saveEmpleado(@RequestBody @Valid EmpleadoModel empleado){
        return this.empleadoService.save(empleado);
    }

    @PutMapping
    @ApiMethod(description = "Sustituye los datos del empleado que corresponda con el mismo numero/codigo")
    public EmpleadoModel updateEmpleado(@RequestBody @Valid EmpleadoModel empleado){

        return this.empleadoService.update(empleado);
    }

    @DeleteMapping
    @ApiMethod(description = "Elimina al empleado que corresponda con el mismo numero/codigo")
    public void deleteEmpleado(@RequestBody EmpleadoModel empleado){
        this.empleadoService.delete(empleado);
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
