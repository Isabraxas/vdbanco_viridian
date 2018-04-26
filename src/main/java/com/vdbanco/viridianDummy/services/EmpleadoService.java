package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.EmpleadoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmpleadoService {
    Page<EmpleadoModel> getAll(Pageable pageable);

    Optional<EmpleadoModel> getById(Long id);

    EmpleadoModel getByEmpleadoNumber(String number);

    EmpleadoModel save(EmpleadoModel empleado);

    EmpleadoModel update(EmpleadoModel empleado);

    void delete(EmpleadoModel empleado);
}
