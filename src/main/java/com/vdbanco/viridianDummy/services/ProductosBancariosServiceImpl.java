package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.ProductosBancariosModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.ProductosBancariosRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductosBancariosServiceImpl implements ProductosBancariosService {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ProductosBancariosServiceImpl.class);
    
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
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.productosBancariosRepository.findById(id);
    }

    @Override
    public ProductosBancariosModel getByProductosBancariosNumber(String number) {
        ProductosBancariosModel productosBancarios = this.productosBancariosRepository.findByProductosBancariosNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(productosBancarios == null) {
            String errorMsg = "El producto Bancario con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return productosBancarios;
    }

    @Override
    public Page<ProductosBancariosModel> getAll(Pageable pageable) {
        return this.productosBancariosRepository.findAllByOrderByProductosBancariosId(pageable);
    }

    @Override
    public ProductosBancariosModel save(ProductosBancariosModel productosBancarios) {
        log.info("Revisando si exite el productosBancarios por number");
        ProductosBancariosModel productosBancariosModel = this.productosBancariosRepository.findByProductosBancariosNumber(productosBancarios.getProductosBancariosNumber());
        if(productosBancariosModel == null) {
            log.info("Creando productosBancarios");
                log.info("Almacenando  productosBancarios");
                this.productosBancariosRepository.save(productosBancarios);

        }else{
            log.error("El productosBancarios con number: "+ productosBancarios.getProductosBancariosNumber() +" ya existe");
            String errorMsg = "El productosBancarios con number: "+ productosBancarios.getProductosBancariosNumber() +" ya existe";
            throw new ConflictsException(errorMsg, new ErrorDetalle(productosBancarios.getProductosBancariosId(),"409","El productosBancarios con number: "+ productosBancarios.getProductosBancariosNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
        }
        return this.getByProductosBancariosNumber(productosBancarios.getProductosBancariosNumber());
    }

    @Override
    public ProductosBancariosModel update(ProductosBancariosModel productosBancarios) {

        log.info("Revisando si exite el productosBancarios por number");
        ProductosBancariosModel currentProductosBancarios = this.getByProductosBancariosNumber(productosBancarios.getProductosBancariosNumber());
        if(currentProductosBancarios != null) {
            log.info("Actualizando productosBancarios");
            //productosBancarios = this.actualizarEntityProductosBancarios(currentProductosBancarios , productosBancarios);

                productosBancarios.setProductosBancariosId(currentProductosBancarios.getProductosBancariosId());
                log.info("Almacenando cambios");
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
