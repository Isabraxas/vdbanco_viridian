package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.JuridicasModel;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.JuridicasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/juridicas")
public class JuridicasController {

    private JuridicasService juridicasService;

    @Autowired
    public JuridicasController(JuridicasService juridicasService) {
        this.juridicasService = juridicasService;
    }

    @GetMapping
    public Page<JuridicasModel> getAllPageable(Pageable pageable){
        return this.juridicasService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<JuridicasModel> getJuridicasById(@PathVariable Long id){
        return this.juridicasService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public JuridicasModel getJuridicasByNumber(@PathVariable String number){
        return this.juridicasService.getByJuridicasNumber(number);
    }

    @PostMapping
    public JuridicasModel saveJuridicas(@RequestBody JuridicasModel juridicas){
        return this.juridicasService.save(juridicas);
    }

    @PutMapping
    public JuridicasModel updateJuridicas(@RequestBody JuridicasModel juridicas){

        return this.juridicasService.update(juridicas);
    }

    @DeleteMapping
    public void deleteJuridicas(@RequestBody JuridicasModel juridicas){
        this.juridicasService.delete(juridicas);
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
