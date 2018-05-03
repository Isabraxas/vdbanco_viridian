package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserModel> getAllPageable(Pageable pageable){
        return this.userService.getAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public Optional<UserModel> getUserById(@PathVariable Long id){
        return this.userService.getById(id);
    }

    @GetMapping(value = "/number/{number}")
    public UserModel getUserByNumber(@PathVariable String number){
        return this.userService.getByUserNumber(number);
    }

    @PostMapping
    public UserModel saveUser(@RequestBody UserModel user){
        return this.userService.save(user);
    }

    @PutMapping
    public UserModel updateUser(@RequestBody UserModel user){

        return this.userService.update(user);
    }

    @DeleteMapping
    public void deleteUser(@RequestBody UserModel user){
        this.userService.delete(user);
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
