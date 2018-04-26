package com.vdbanco.viridianDummy.repository;

import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductosBancariosRepository extends JpaRepository<ProductosBancariosModel, Long>, PagingAndSortingRepository <ProductosBancariosModel, Long>{

    ProductosBancariosModel findByProductosBancariosNumber(String number);

    Page<ProductosBancariosModel> findAllByOrderByProductosBancariosId(Pageable pageable);
}
