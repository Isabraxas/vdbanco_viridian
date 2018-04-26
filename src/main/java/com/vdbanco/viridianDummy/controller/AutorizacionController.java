package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import com.vdbanco.viridianDummy.services.AutorizacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/autorizacions")
public class AutorizacionController {

    private AutorizacionService autorizacionService;

    @Autowired
    public AutorizacionController(AutorizacionService autorizacionService) {
        this.autorizacionService = autorizacionService;
    }

    @GetMapping
    public Page<AutorizacionModel> getAllPageable(Pageable pageable){
        return this.autorizacionService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<AutorizacionModel> getAutorizacionById(@PathVariable Long id){
        return this.autorizacionService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public AutorizacionModel getAutorizacionByNumber(@PathVariable String number){
        return this.autorizacionService.getByAutorizacionNumber(number);
    }

    @PostMapping
    public AutorizacionModel saveAutorizacion(@RequestBody AutorizacionModel autorizacion){
        return this.autorizacionService.save(autorizacion);
    }

    @PutMapping
    public AutorizacionModel updateAutorizacion(@RequestBody AutorizacionModel autorizacion){

        return this.autorizacionService.update(autorizacion);
    }

    @DeleteMapping
    public void deleteAutorizacion(@RequestBody AutorizacionModel autorizacion){
        this.autorizacionService.delete(autorizacion);
    }

}
