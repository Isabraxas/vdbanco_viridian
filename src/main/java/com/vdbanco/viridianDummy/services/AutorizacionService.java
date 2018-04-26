package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.AutorizacionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AutorizacionService {
    Page<AutorizacionModel> getAll(Pageable pageable);

    Optional<AutorizacionModel> getById(Long id);

    AutorizacionModel getByAutorizacionNumber(String number);

    AutorizacionModel save(AutorizacionModel autorizacion);

    AutorizacionModel update(AutorizacionModel autorizacion);

    void delete(AutorizacionModel autorizacion);
}
