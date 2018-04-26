package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AccountHolderService {
    Optional<AccountHolderModel> getById(Long id);

    AccountHolderModel getByAccountHolderNumber(String number);

    AccountHolderModel getAccountHolderByPersonaNumber(String number);

    AccountHolderModel save(AccountHolderModel accountHolder);

    AccountHolderModel update(AccountHolderModel accountHolder);

    void delete(AccountHolderModel accountHolder);

    Page<AccountHolderModel> getAll(Pageable pageable);
}
