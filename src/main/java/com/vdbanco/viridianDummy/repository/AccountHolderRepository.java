package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountHolderRepository extends JpaRepository<AccountHolderModel,Long> {
    AccountHolderModel findByAccountHolderNumber(String number);

    Page<AccountHolderModel> findAllByOrderByAccountHolderId(Pageable pageable);

    AccountHolderModel findAllByPersonaPersonaNumber(String number);

    AccountHolderModel findAllByPersonaPersonaNumberOrAccountHolderTitularNumberOrAccountHolderApoderado(String numberA, String numberB, String numberC);

    @Transactional
    void deleteByAccountHolderNumber(String accountHolderNumber);
}
