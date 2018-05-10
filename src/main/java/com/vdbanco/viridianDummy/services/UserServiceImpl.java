package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.userRepository.findById(id);
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
        UserModel userModel = this.userRepository.findByUserNumber(user.getUserNumber());
        if(userModel == null) {
            PersonaModel persona = this.personaService.getByPersonaNumber(user.getPersonaPersonaNumber());
            if(persona != null) {
                user.setPersona(persona);
                this.userRepository.save(user);
            }
        }else{

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
        UserModel userModel = this.getByUserNumber(user.getUserNumber());
        if(userModel != null) {
            user = this.actualizarEntityUser(userModel , user);
            PersonaModel persona = this.personaService.getByPersonaNumber(user.getPersonaPersonaNumber());
            if(persona != null) {
                user.setUserId(userModel.getUserId());
                user.setPersona(persona);
                this.userRepository.save(user);
                return this.getByUserNumber(user.getUserNumber());
            }
        }
        return null;
    }

    @Override
    public void delete(UserModel user) {
        this.userRepository.deleteById(user.getUserId());
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
