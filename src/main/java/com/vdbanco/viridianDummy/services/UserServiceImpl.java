package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private PersonaService personaService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PersonaService personaService) {
        this.userRepository = userRepository;
        this.personaService = personaService;
    }

    @Override
    public Optional<UserModel> getById(Long id) {
        Optional<UserModel> user = this.userRepository.findById(id);
        if(!user.isPresent()) {
            String errorMsg = "El user con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "404", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.userRepository.findById(id);
    }

    @Override
    public UserModel getByUserName(String username) {
        UserModel userModel = this.userRepository.findByUserName(username);
        if(userModel == null){
            String errorMsg = "El user con UserName: "+ username +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(1L, "404", "El user con UserName: "+ username +" no fue encontrado", "Hemos encontrado un error intentelo mas tarde"));
        }else {
            return userModel;
        }

    }

    @Override
    public UserModel getByUserNumber(String number) {
        UserModel user = this.userRepository.findByUserNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(user == null) {
            String errorMsg = "El user con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "404", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return user;
    }

    @Override
    public UserModel save(UserModel user) {
        log.info("Revisando si exite el user por number");
        UserModel userModel = this.userRepository.findByUserNumber(user.getUserNumber());
        UserModel userModel1 = this.userRepository.findByUserName(user.getUserName());

        if(userModel == null || userModel1 == null) {
            log.info("Creando user");
            PersonaModel persona = this.personaService.getByPersonaNumber(user.getPersonaPersonaNumber());
            if(persona != null) {
                user.setUserId(null);
                user.setPersona(persona);
                log.info("Almacenando  user");
                this.userRepository.save(user);
            }
        }else{
            log.error("El user con number: "+ user.getUserNumber() +" ya existe");
            String errorMsg = "El user con number: "+ user.getUserNumber() +" ya existe";
            throw new ConflictsException(errorMsg, new ErrorDetalle(user.getUserId(),"409","El user con number: "+ user.getUserNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
        }
        return this.getByUserNumber(user.getUserNumber());
    }

    @Override
    public Page<UserModel> getAll(Pageable pageable) {
        return this.userRepository.findAllByOrderByUserId(pageable);
    }

    @Override
    public UserModel update(UserModel user) {

        log.info("Revisando si exite el user por number");
        UserModel currentUser = this.getByUserNumber(user.getUserNumber());
        if(currentUser != null) {
            log.info("Actualizando user");
            user = this.actualizarEntityUser(currentUser , user);
            PersonaModel persona = this.personaService.getByPersonaNumber(user.getPersonaPersonaNumber());
            if(persona != null) {
                user.setUserId(currentUser.getUserId());
                user.setPersona(persona);
                log.info("Almacenando cambios");
                this.userRepository.save(user);
                return this.getByUserNumber(user.getUserNumber());
            }
        }
        return null;
    }

    @Override
    public void delete(UserModel user) {
        //this.userRepository.deleteById(user.getUserId());
        this.userRepository.deleteByUserNumber(user.getUserNumber());
    }

    public UserModel actualizarEntityUser(UserModel currentUser, UserModel newUser){
        if(newUser.getUserId() == null){
            newUser.setUserId(currentUser.getUserId());
        }
        if(newUser.getPersonaPersonaNumber() == null){
           newUser.setPersonaPersonaNumber(currentUser.getPersonaPersonaNumber());
        }
        if(newUser.getUserName() == null){
            newUser.setUserName(currentUser.getUserName());
        }
        if(newUser.getUserPassword() == null){
            newUser.setUserPassword(currentUser.getUserPassword());
        }
        if(newUser.getUserCreateTime() == null){
            newUser.setUserCreateTime(currentUser.getUserCreateTime());
        }
        return  newUser;
    }
}
