package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.domain.UserModelList;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.EntidadError;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.services.UserService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiStage;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/users")
@Api(
        name = "Usuarios",
        description = "Permite manejar por medio de una lista de metodos las operaciones aplicadas a los usuarios del banco.",
        stage = ApiStage.ALPHA
)
public class UserController {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserController.class);
    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping
    @ApiMethod(description = "Retorna una lista de usuarios paginados de 20 en 20")
    public UserModelList  getAllPageable(@ApiQueryParam(name = "page", description = "numero de pagina", required = false, defaultvalue = "0")
                                             @RequestParam( required = false, defaultValue = "0") int page,
                                         @ApiQueryParam(name = "size", description = "cantidad de elementos", required = false, defaultvalue = "20")
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
    @ApiMethod(description = "Retorna el usuario correspondiente con id proporcionado en el path.")
    public Optional<UserModel> getUserById(@ApiPathParam(name="id",description = "id de usuario") @PathVariable Long id){
        Pageable pageable = null;
        Optional<UserModel> user = this.userService.getById(id);
        user.get().add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(user.get().getPersonaPersonaNumber())).withRel("Persona detalle"));
        user.get().add(linkTo(methodOn(UserController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de eventos"));
        return user;
    }

    @GetMapping(value = "/number/{number}", produces = MediaTypes.HAL_JSON_VALUE)
    @ApiMethod(description = "Retorna el usuario correspondiente con el numero/codigo proporcionado en el path.")
    public UserModel getUserByNumber(@ApiPathParam(name = "number" , description = "numero o codigo de cuenta") @PathVariable String number){

        UserModel user = this.userService.getByUserNumber(number);
        Link link = ControllerLinkBuilder.linkTo(UserController.class).slash("number").slash(user.getUserNumber()).withSelfRel();
        user.add(link);
        user.add(linkTo(methodOn(PersonaController.class).getPersonaByNumber(user.getPersonaPersonaNumber())).withRel("Persona detalle"));
        user.add(linkTo(methodOn(UserController.class).getAllPageable(0, Integer.parseInt(env.getProperty("spring.data.rest.max-page-size")))).withRel("Lista de usuarios"));
        return user;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    @ApiMethod(description = "Almacena un nuevo usuario con los datos ingresados.", responsestatuscode = "201" )
    public  UserModel saveUser(@ApiBodyObject @RequestBody @Valid UserModel user){
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        return this.userService.save(user);
    }

    @PutMapping
    @ApiMethod(description = "Sustituye los datos del usuario que corresponda con el mismo numero/codigo")
    public UserModel updateUser(@RequestBody @Valid UserModel user){
        return this.userService.update(user);
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping
    @ApiMethod(description = "Elimina al usuario que corresponda con el mismo numero/codigo")
    public void deleteUser(@RequestBody UserModel user){
        this.userService.delete(user);
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
