package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserModel> getById(Long id) {
        Optional<UserModel> user = this.userRepository.findById(id);
        if(!user.isPresent()) {
            String errorMsg = "El user con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.userRepository.findById(id);
    }

    @Override
    public UserModel getByUserNumber(String number) {
        UserModel user = this.userRepository.findByUserNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(user == null) {
            String errorMsg = "El user con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return user;
    }

    @Override
    public UserModel save(UserModel user) {
        boolean existe = this.userRepository.existsById(user.getUserId());
        if(!existe) {
            this.userRepository.save(user);
        }
        return this.getByUserNumber(user.getUserNumber());
    }

    @Override
    public Page<UserModel> getAll(Pageable pageable) {
        return this.userRepository.findAllByOrderByUserId(pageable);
    }

    @Override
    public UserModel update(UserModel user) {
        boolean existe = this.userRepository.existsById(user.getUserId());
        if(existe) {
            this.userRepository.save(user);
            return this.getByUserNumber(user.getUserNumber());
        }
        return null;
    }

    @Override
    public void delete(UserModel user) {
        this.userRepository.deleteById(user.getUserId());
    }
}
