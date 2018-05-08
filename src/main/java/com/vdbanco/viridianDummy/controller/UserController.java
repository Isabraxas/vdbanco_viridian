package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.domain.UserModelList;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserModelList  getAllPageable(Pageable pageable){
        Page<UserModel> userPages = this.userService.getAll(pageable);
        List<UserModel> userList = userPages.getContent();
        for (UserModel user: userList ) {
            user.add(linkTo(methodOn(UserController.class).getUserByNumber(user.getUserNumber())).withSelfRel());
        }
        UserModelList userModelList = new UserModelList(userList);
        log.info("Pageable next= " + pageable);
        log.info("Pageable next= " + pageable.next());
        pageable = pageable.next();
        //Link link = ControllerLinkBuilder.linkTo(methodOn(UserController.class).getAllPageable(pageable1)).withRel("nextPage");

        userModelList.setNextPage("http://localhost:8081/users?page=" +1);
        return userModelList;
    }

    @GetMapping(value = "/{id}")
    public Optional<UserModel> getUserById(@PathVariable Long id){
        Pageable pageable = null;
        Optional<UserModel> user = this.userService.getById(id);
        user.get().add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(user.get().getPersonaPersonaNumber())).withRel("Persona detalle"));
        user.get().add(linkTo(methodOn(UserController.class).getAllPageable(pageable)).withRel("Lista de eventos"));
        return user;
    }

    @GetMapping(value = "/number/{number}", produces = MediaTypes.HAL_JSON_VALUE)
    public UserModel getUserByNumber(@PathVariable String number){

        Pageable pageable = null;
        UserModel user = this.userService.getByUserNumber(number);
        Link link = ControllerLinkBuilder.linkTo(UserController.class).slash("number").slash(user.getUserNumber()).withSelfRel();
        user.add(link);
        user.add(linkTo(methodOn(UserController.class).getAllPageable(pageable)).withRel("Lista de eventos"));
        return user;
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
