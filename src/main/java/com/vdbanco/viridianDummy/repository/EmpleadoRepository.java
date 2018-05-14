package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.EmpleadoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface EmpleadoRepository extends JpaRepository<EmpleadoModel, Long> {
    EmpleadoModel findByEmpleadoNumber(String number);

    Page<EmpleadoModel> findAllByOrderByEmpleadoId(Pageable pageable);

    @Transactional
    void deleteByEmpleadoNumber(String empleadoNumber);
}
