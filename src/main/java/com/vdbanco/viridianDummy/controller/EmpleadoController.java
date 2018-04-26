package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.EmpleadoModel;
import com.vdbanco.viridianDummy.services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/empleados")
public class EmpleadoController {

    private EmpleadoService empleadoService;

    @Autowired
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public Page<EmpleadoModel> getAllPageable(Pageable pageable){
        return this.empleadoService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<EmpleadoModel> getEmpleadoById(@PathVariable Long id){
        return this.empleadoService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public EmpleadoModel getEmpleadoByNumber(@PathVariable String number){
        return this.empleadoService.getByEmpleadoNumber(number);
    }

    @PostMapping
    public EmpleadoModel saveEmpleado(@RequestBody EmpleadoModel empleado){
        return this.empleadoService.save(empleado);
    }

    @PutMapping
    public EmpleadoModel updateEmpleado(@RequestBody EmpleadoModel empleado){

        return this.empleadoService.update(empleado);
    }

    @DeleteMapping
    public void deleteEmpleado(@RequestBody EmpleadoModel empleado){
        this.empleadoService.delete(empleado);
    }
}
