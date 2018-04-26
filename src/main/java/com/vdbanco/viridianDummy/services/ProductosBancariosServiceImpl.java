package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
import com.vdbanco.viridianDummy.error.ErrorNoEncontrado;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.ProductosBancariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductosBancariosServiceImpl implements ProductosBancariosService {
    private ProductosBancariosRepository productosBancariosRepository;

    @Autowired
    public ProductosBancariosServiceImpl(ProductosBancariosRepository productosBancariosRepository) {
        this.productosBancariosRepository = productosBancariosRepository;
    }

    @Override
    public Optional<ProductosBancariosModel> getById(Long id) {
        Optional<ProductosBancariosModel> productosBancarios = this.productosBancariosRepository.findById(id);
        if(!productosBancarios.isPresent()) {
            String errorMsg = "El producto Bancario con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.productosBancariosRepository.findById(id);
    }

    @Override
    public ProductosBancariosModel getByProductosBancariosNumber(String number) {
        ProductosBancariosModel productosBancarios = this.productosBancariosRepository.findByProductosBancariosNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(productosBancarios == null) {
            String errorMsg = "El producto Bancario con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorNoEncontrado(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return productosBancarios;
    }

    @Override
    public ProductosBancariosModel save(ProductosBancariosModel productosBancarios) {
        boolean existe = this.productosBancariosRepository.existsById(productosBancarios.getProductosBancariosId());
        if(!existe) {
            this.productosBancariosRepository.save(productosBancarios);
        }
        return this.getByProductosBancariosNumber(productosBancarios.getProductosBancariosNumber());
    }

    @Override
    public Page<ProductosBancariosModel> getAll(Pageable pageable) {
        return this.productosBancariosRepository.findAllByOrderByProductosBancariosId(pageable);
    }

    @Override
    public ProductosBancariosModel update(ProductosBancariosModel productosBancarios) {
        boolean existe = this.productosBancariosRepository.existsById(productosBancarios.getProductosBancariosId());
        if(existe) {
            this.productosBancariosRepository.save(productosBancarios);
            return this.getByProductosBancariosNumber(productosBancarios.getProductosBancariosNumber());
        }
        return null;
    }

    @Override
    public void delete(ProductosBancariosModel productosBancarios) {
        this.productosBancariosRepository.deleteById(productosBancarios.getProductosBancariosId());
    }
}
