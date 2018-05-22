package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.JuridicasModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JuridicasService {
    Page<JuridicasModel> getAll(Pageable pageable);

    Optional<JuridicasModel> getById(Long id);

    JuridicasModel getByJuridicasNumber(String number);

    JuridicasModel save(JuridicasModel juridicas);

    JuridicasModel update(JuridicasModel juridicas);

    JuridicasModel getByRazonSocial(String razonSocial);

    void delete(JuridicasModel juridicas);
}
