package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.domain.UserModelList;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;


    @GetMapping
    public UserModelList  getAllPageable(@RequestParam( required = false, defaultValue = "0") int page,
                                         @RequestParam( required = false, defaultValue = "20") int size ){

        PageRequest pageRequest = PageRequest.of(page,size);
        Page<UserModel> userPages = this.userService.getAll(pageRequest);
        List<UserModel> userList = userPages.getContent();

        for (UserModel user: userList ) {
            user.add(linkTo(methodOn(UserController.class).getUserByNumber(user.getUserNumber())).withSelfRel());
        }

        UserModelList userModelList = new UserModelList(userList);

        Link linkNext = ControllerLinkBuilder.linkTo(methodOn(UserController.class).getAllPageable(pageRequest.next().getPageNumber(),pageRequest.getPageSize())).withRel("next page");
        Link linkPrevious = ControllerLinkBuilder.linkTo(methodOn(UserController.class).getAllPageable(pageRequest.previousOrFirst().getPageNumber(),pageRequest.getPageSize())).withRel("previous page");
        userModelList.add(linkNext);
        userModelList.add(linkPrevious);
        return userModelList;
    }

    @GetMapping(value = "/{id}")
    public Optional<UserModel> getUserById(@PathVariable Long id){
        Pageable pageable = null;
        Optional<UserModel> user = this.userService.getById(id);
        user.get().add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(user.get().getPersonaPersonaNumber())).withRel("Persona detalle"));
        user.get().add(linkTo(methodOn(UserController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de eventos"));
        return user;
    }

    @GetMapping(value = "/number/{number}", produces = MediaTypes.HAL_JSON_VALUE)
    public UserModel getUserByNumber(@PathVariable String number){

        UserModel user = this.userService.getByUserNumber(number);
        Link link = ControllerLinkBuilder.linkTo(UserController.class).slash("number").slash(user.getUserNumber()).withSelfRel();
        user.add(link);
        user.add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(user.getPersonaPersonaNumber())).withRel("Persona detalle"));
        user.add(linkTo(methodOn(UserController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de usuarios"));
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
