package com.vdbanco.viridianDummy.domain;

import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "EMPLEADO")
@Entity
public class EmpleadoModel extends ResourceSupport implements Serializable {

    @Id
    private Long empleadoId;
    private String empleadoNumber;
    private String empleadoUsername;
    private String empleadoCargo;
    private String empleadoAgencia;
    private String empleadoGrupo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSONA_PERSONA_ID")
    private PersonaModel persona;
    private String personaPersonaNumber;

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getEmpleadoNumber() {
        return empleadoNumber;
    }

    public void setEmpleadoNumber(String empleadoNumber) {
        this.empleadoNumber = empleadoNumber;
    }

    public String getEmpleadoUsername() {
        return empleadoUsername;
    }

    public void setEmpleadoUsername(String empleadoUsername) {
        this.empleadoUsername = empleadoUsername;
    }

    public String getEmpleadoCargo() {
        return empleadoCargo;
    }

    public void setEmpleadoCargo(String empleadoCargo) {
        this.empleadoCargo = empleadoCargo;
    }

    public String getEmpleadoAgencia() {
        return empleadoAgencia;
    }

    public void setEmpleadoAgencia(String empleadoAgencia) {
        this.empleadoAgencia = empleadoAgencia;
    }

    public String getEmpleadoGrupo() {
        return empleadoGrupo;
    }

    public void setEmpleadoGrupo(String empleadoGrupo) {
        this.empleadoGrupo = empleadoGrupo;
    }

    public PersonaModel getPersona() {
        return persona;
    }

    public void setPersona(PersonaModel persona) {
        this.persona = persona;
    }

    public String getPersonaPersonaNumber() {
        return personaPersonaNumber;
    }

    public void setPersonaPersonaNumber(String personaPersonaNumber) {
        this.personaPersonaNumber = personaPersonaNumber;
    }
}
