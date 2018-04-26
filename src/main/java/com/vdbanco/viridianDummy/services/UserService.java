package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<UserModel> getAll(Pageable pageable);

    Optional<UserModel> getById(Long id);

    UserModel getByUserNumber(String number);

    UserModel save(UserModel user);

    UserModel update(UserModel user);

    void delete(UserModel user);
}
