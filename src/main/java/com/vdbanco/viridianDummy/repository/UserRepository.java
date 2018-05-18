package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<UserModel, Long> , PagingAndSortingRepository<UserModel, Long>{

    UserModel findByUserName(String username);

    UserModel findByUserNumber(String number);

    Page<UserModel> findAllByOrderByUserId(Pageable pageable);

    @Transactional
    void deleteByUserNumber(String userNumber);
}
