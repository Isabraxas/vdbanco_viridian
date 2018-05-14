package com.vdbanco.viridianDummy.services;

import com.vdbanco.viridianDummy.domain.EmpleadoModel;
import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.error.ConflictsException;
import com.vdbanco.viridianDummy.error.ErrorDetalle;
import com.vdbanco.viridianDummy.error.NoEncontradoRestException;
import com.vdbanco.viridianDummy.repository.EmpleadoRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    // logger
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(EmpleadoServiceImpl.class);

    private EmpleadoRepository empleadoRepository;
    private PersonaService personaService;

    @Autowired
    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, PersonaService personaService) {
        this.empleadoRepository = empleadoRepository;
        this.personaService = personaService;
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
    public Page<EmpleadoModel> getAll(Pageable pageable) {
        return this.empleadoRepository.findAllByOrderByEmpleadoId(pageable);
    }


    @Override
    public EmpleadoModel save(EmpleadoModel empleado) {
        log.info("Revisando si exite el empleado por number");
        EmpleadoModel empleadoModel = this.empleadoRepository.findByEmpleadoNumber(empleado.getEmpleadoNumber());
        if(empleadoModel == null) {
            log.info("Creando empleado");
            PersonaModel persona = this.personaService.getByPersonaNumber(empleado.getPersonaPersonaNumber());
            if(persona != null) {
                empleado.setPersona(persona);
                log.info("Almacenando  empleado");
                this.empleadoRepository.save(empleado);
            }
        }else{
            log.error("El empleado con number: "+ empleado.getEmpleadoNumber() +" ya existe");
            String errorMsg = "El empleado con number: "+ empleado.getEmpleadoNumber() +" ya existe";
            throw new ConflictsException(errorMsg, new ErrorDetalle(empleado.getEmpleadoId(),"409","El empleado con number: "+ empleado.getEmpleadoNumber() +" ya existe","Hemos encontrado un error intentelo nuevamente"));
        }
        return this.getByEmpleadoNumber(empleado.getEmpleadoNumber());
    }
    
    @Override
    public EmpleadoModel update(EmpleadoModel empleado) {

        log.info("Revisando si exite el empleado por number");
        EmpleadoModel currentEmpleado = this.getByEmpleadoNumber(empleado.getEmpleadoNumber());
        if(currentEmpleado != null) {
            log.info("Actualizando empleado");
            //empleado = this.actualizarEntityEmpleado(currentEmpleado , empleado);
            PersonaModel persona = this.personaService.getByPersonaNumber(empleado.getPersonaPersonaNumber());
            if(persona != null) {
                empleado.setEmpleadoId(currentEmpleado.getEmpleadoId());
                empleado.setPersona(persona);
                log.info("Almacenando cambios");
                this.empleadoRepository.save(empleado);
                return this.getByEmpleadoNumber(empleado.getEmpleadoNumber());
            }
        }
        return null;
    }

    @Override
    public void delete(EmpleadoModel empleado) {
        //this.empleadoRepository.deleteById(empleado.getEmpleadoId());
        this.empleadoRepository.deleteByEmpleadoNumber(empleado.getEmpleadoNumber());
    }
}
