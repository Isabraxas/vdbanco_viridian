package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.EmpleadoModel;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {
    private EmpleadoRepository empleadoRepository;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Optional<EmpleadoModel> getById(Long id) {
        Optional<EmpleadoModel> empleado = this.empleadoRepository.findById(id);
        if(!empleado.isPresent()) {
            String errorMsg = "El empleado con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return this.empleadoRepository.findById(id);
    }

    @Override
    public EmpleadoModel getByEmpleadoNumber(String number) {
        EmpleadoModel empleado = this.empleadoRepository.findByEmpleadoNumber(number);
        Long id= Long.valueOf(number.substring(4));
        if(empleado == null) {
            String errorMsg = "El empleado con Id: "+ id +" no fue encontrado";
            throw new NoEncontradoRestException(errorMsg, new ErrorDetalle(id, "001", "no se encontro en la BD", "Hemos encontrado un error intentelo mas tarde"));
        }
        return empleado;
    }

    @Override
    public EmpleadoModel save(EmpleadoModel empleado) {
        boolean existe = this.empleadoRepository.existsById(empleado.getEmpleadoId());
        if(!existe) {
            this.empleadoRepository.save(empleado);
        }
        return this.getByEmpleadoNumber(empleado.getEmpleadoNumber());
    }

    @Override
    public Page<EmpleadoModel> getAll(Pageable pageable) {
        return this.empleadoRepository.findAllByOrderByEmpleadoId(pageable);
    }

    @Override
    public EmpleadoModel update(EmpleadoModel empleado) {
        boolean existe = this.empleadoRepository.existsById(empleado.getEmpleadoId());
        if(existe) {
            this.empleadoRepository.save(empleado);
            return this.getByEmpleadoNumber(empleado.getEmpleadoNumber());
        }
        return null;
    }

    @Override
    public void delete(EmpleadoModel empleado) {
        this.empleadoRepository.deleteById(empleado.getEmpleadoId());
    }
}
