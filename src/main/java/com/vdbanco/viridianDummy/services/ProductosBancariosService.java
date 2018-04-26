package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductosBancariosService {
    Page<ProductosBancariosModel> getAll(Pageable pageable);

    Optional<ProductosBancariosModel> getById(Long id);

    ProductosBancariosModel getByProductosBancariosNumber(String number);

    ProductosBancariosModel save(ProductosBancariosModel productosBancarios);

    ProductosBancariosModel update(ProductosBancariosModel productosBancarios);

    void delete(ProductosBancariosModel productosBancarios);
}
