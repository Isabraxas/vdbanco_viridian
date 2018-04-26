package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AutorizacionRepository extends JpaRepository<AutorizacionModel, Long> , PagingAndSortingRepository<AutorizacionModel, Long> {

    AutorizacionModel findByAutorizacionNumber(String number);

    Page<AutorizacionModel> findAllByOrderByAutorizacionId(Pageable pageable);

}
