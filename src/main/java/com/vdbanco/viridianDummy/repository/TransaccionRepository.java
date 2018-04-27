package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TransaccionRepository extends JpaRepository<TransaccionModel, Long> , PagingAndSortingRepository<TransaccionModel, Long> {

    List<TransaccionModel> findByTransaccionNumber(String number);

    List<TransaccionModel> findByAccountNumber(String accountNumber);

    //@Query(value = "select t from TransaccionModel t where t.accountNumber = ?1 and FUNCTION('MONTH',t.transaccionDate) = ?2")
    //List<TransaccionModel> findByAccountNumberAndLastMonths(String accountNumber,  Integer numberMonth);

    @Query(value = "select t from TransaccionModel t where t.accountNumber = ?1 and FUNCTION('TRUNC',t.transaccionDate , 'MM') = FUNCTION('TRUNC',FUNCTION('ADD_MONTHS',function('SYSDATE'), ?2*(-1)) , 'MM')")
    List<TransaccionModel> findByAccountNumberAndLastMonths(String accountNumber,  int numberMonth);

    List<TransaccionModel> findTop10ByAccountNumberOrderByTransaccionDateDesc(String accountNumber);

    List<TransaccionModel> findByAccountNumberAndTransaccionDateBetween(String accountNumber, Timestamp transaccionDateI, Timestamp transaccionDateF );

    Page<TransaccionModel> findAllByOrderByTransaccionId(Pageable pageable);

}
