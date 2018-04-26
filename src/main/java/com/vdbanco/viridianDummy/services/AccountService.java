package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AccountModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Page<AccountModel> getAll(Pageable pageable);

    Optional<AccountModel> getById(Long id);

    AccountModel getByAccountNumber(String number);

    AccountModel save(AccountModel account);

    List<AccountModel> getAccountByAccountHolder(String number);

    AccountModel update(AccountModel account);

    void delete(AccountModel account);
}
