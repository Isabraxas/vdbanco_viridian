package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<TransaccionModel, Long> , PagingAndSortingRepository<TransaccionModel, Long> {

    List<TransaccionModel> findByTransaccionNumber(String number);

    Page<TransaccionModel> findAllByOrderByTransaccionId(Pageable pageable);
}
