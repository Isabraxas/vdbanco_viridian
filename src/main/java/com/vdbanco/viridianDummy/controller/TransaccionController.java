package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public TransaccionModel saveTransaccion(@RequestBody TransaccionModel transaccion){
        return this.transaccionService.save(transaccion);
    }

    @PutMapping
    public TransaccionModel updateTransaccion(@RequestBody TransaccionModel transaccion){

        return this.transaccionService.update(transaccion);
    }

    @DeleteMapping
    public void deleteTransaccion(@RequestBody TransaccionModel transaccion){
        this.transaccionService.delete(transaccion);
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
