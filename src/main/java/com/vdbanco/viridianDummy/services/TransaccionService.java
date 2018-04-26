package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.TransaccionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TransaccionService {
    Page<TransaccionModel> getAll(Pageable pageable);

    Optional<TransaccionModel> getById(Long id);

    List<TransaccionModel> getByTransaccionNumber(String number);

    List<TransaccionModel> save(TransaccionModel transaccion);

    List<TransaccionModel> update(TransaccionModel transaccion);

    void delete(TransaccionModel transaccion);
}
