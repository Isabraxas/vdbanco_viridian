package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.AccountModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountModel,Long> {
    AccountModel findByAccountNumber(String number);

    Page<AccountModel> findAllByOrderByAccountId(Pageable pageable);

    List<AccountModel> findByAccountHolderNumber(String number);

    AccountModel findByAccountNumberAndProductosBancariosNumberIn(String number, List<String> numberProductos);

    boolean existsByAccountNumber(String accountNumber);
}
