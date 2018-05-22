package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.JuridicasModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JuridicasRepository extends JpaRepository<JuridicasModel, Long> , PagingAndSortingRepository<JuridicasModel, Long> {

    JuridicasModel findByJuridicasNumber(String number);

    Page<JuridicasModel> findAllByOrderByJuridicasId(Pageable pageable);

    JuridicasModel findByJuridicasRazonSocial(String razonSocial);

    @Transactional
    void deleteByJuridicasNumber(String juridicasNumber);
    @Transactional
    void deleteAllByJuridicasNumber(String juridicasNumber);
}
