package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.services.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/transaccions")
public class TransaccionController {

    private TransaccionService transaccionService;

    @Autowired
    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public Page<TransaccionModel> getAllPageable(Pageable pageable){
        return this.transaccionService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<TransaccionModel> getTransaccionById(@PathVariable Long id){
        return this.transaccionService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public List<TransaccionModel> getTransaccionByNumber(@PathVariable String number){
        return this.transaccionService.getByTransaccionNumber(number);
    }

    @PostMapping
    public List<TransaccionModel> saveTransaccion(@RequestBody TransaccionModel transaccion){
        return this.transaccionService.save(transaccion);
    }

    @PutMapping
    public List<TransaccionModel> updateTransaccion(@RequestBody TransaccionModel transaccion){

        return this.transaccionService.update(transaccion);
    }

    @DeleteMapping
    public void deleteTransaccion(@RequestBody TransaccionModel transaccion){
        this.transaccionService.delete(transaccion);
    }

}
