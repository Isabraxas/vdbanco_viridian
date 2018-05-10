package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

@Table(name="PERSONA")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PersonaModel extends ResourceSupport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSONA_SEQ")
    @SequenceGenerator(sequenceName = "PERSONA_PERSONA_ID_SEQ", allocationSize = 1, name = "PERSONA_SEQ")
    private Long personaId;

    @NotNull
    private String personaNumber;
    private String personaNombre;
    private String personaNit;
    private String personaApellidoPaterno;
    private String personaApellidoMaterno;
    private String personaApellidoCazada;
    private String personaNombres;
    private String personaDocumentoIdentidad;
    private String personaDocumentoIdentidadNu;

    @Column(name="PERSONA_FECHA_DE_NACIMIENTO")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Timestamp personaFechaDeNacimiento;
    private String personaLugarDeNacimiento;
    private String personaNacionalidad;
    private String personaDomicilioActual;
    private String personaTrabajo;
    private String personaTrabajoCargo;
    private String personaTrabajoDomicilio;
    private String personaTelefono;
    private String personaCelular;
    private String personaTrabajoTelefono;
    private String personaTrabajoCelular;
    private String personaCorreoElectronico;
    private String personaEstadoCivil;
    private String personaProfecioin;
    private String personaNombresApellidosCony;
    private String personaNombresApellidosMadr;
    private String personaNombresApellidosPadr;
    private String personaNombresApellidosRef1;
    private String personaNombresApellidosRef2;
    private String personaNombresApellidosRef3;
    private Blob personaFirma1;
    private Blob personaFirma2;
    private Blob personaFirma3;


    public Long getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Long personaId) {
        this.personaId = personaId;
    }

    public String getPersonaNumber() {
        return personaNumber;
    }

    public void setPersonaNumber(String personaNumber) {
        this.personaNumber = personaNumber;
    }

    public String getPersonaNombre() {
        return personaNombre;
    }

    public void setPersonaNombre(String personaNombre) {
        this.personaNombre = personaNombre;
    }

    public String getPersonaNit() {
        return personaNit;
    }

    public void setPersonaNit(String personaNit) {
        this.personaNit = personaNit;
    }

    public String getPersonaApellidoPaterno() {
        return personaApellidoPaterno;
    }

    public void setPersonaApellidoPaterno(String personaApellidoPaterno) {
        this.personaApellidoPaterno = personaApellidoPaterno;
    }

    public String getPersonaApellidoMaterno() {
        return personaApellidoMaterno;
    }

    public void setPersonaApellidoMaterno(String personaApellidoMaterno) {
        this.personaApellidoMaterno = personaApellidoMaterno;
    }

    public String getPersonaApellidoCazada() {
        return personaApellidoCazada;
    }

    public void setPersonaApellidoCazada(String personaApellidoCazada) {
        this.personaApellidoCazada = personaApellidoCazada;
    }

    public String getPersonaNombres() {
        return personaNombres;
    }

    public void setPersonaNombres(String personaNombres) {
        this.personaNombres = personaNombres;
    }

    public String getPersonaDocumentoIdentidad() {
        return personaDocumentoIdentidad;
    }

    public void setPersonaDocumentoIdentidad(String personaDocumentoIdentidad) {
        this.personaDocumentoIdentidad = personaDocumentoIdentidad;
    }

    public String getPersonaDocumentoIdentidadNu() {
        return personaDocumentoIdentidadNu;
    }

    public void setPersonaDocumentoIdentidadNu(String personaDocumentoIdentidadNu) {
        this.personaDocumentoIdentidadNu = personaDocumentoIdentidadNu;
    }

    public Timestamp getPersonaFechaDeNacimiento() {
        return personaFechaDeNacimiento;
    }

    public void setPersonaFechaDeNacimiento(Timestamp personaFechaDeNacimiento) {
        this.personaFechaDeNacimiento = personaFechaDeNacimiento;
    }

    public String getPersonaLugarDeNacimiento() {
        return personaLugarDeNacimiento;
    }

    public void setPersonaLugarDeNacimiento(String personaLugarDeNacimiento) {
        this.personaLugarDeNacimiento = personaLugarDeNacimiento;
    }

    public String getPersonaNacionalidad() {
        return personaNacionalidad;
    }

    public void setPersonaNacionalidad(String personaNacionalidad) {
        this.personaNacionalidad = personaNacionalidad;
    }

    public String getPersonaDomicilioActual() {
        return personaDomicilioActual;
    }

    public void setPersonaDomicilioActual(String personaDomicilioActual) {
        this.personaDomicilioActual = personaDomicilioActual;
    }

    public String getPersonaTrabajo() {
        return personaTrabajo;
    }

    public void setPersonaTrabajo(String personaTrabajo) {
        this.personaTrabajo = personaTrabajo;
    }

    public String getPersonaTrabajoCargo() {
        return personaTrabajoCargo;
    }

    public void setPersonaTrabajoCargo(String personaTrabajoCargo) {
        this.personaTrabajoCargo = personaTrabajoCargo;
    }

    public String getPersonaTrabajoDomicilio() {
        return personaTrabajoDomicilio;
    }

    public void setPersonaTrabajoDomicilio(String personaTrabajoDomicilio) {
        this.personaTrabajoDomicilio = personaTrabajoDomicilio;
    }

    public String getPersonaTelefono() {
        return personaTelefono;
    }

    public void setPersonaTelefono(String personaTelefono) {
        this.personaTelefono = personaTelefono;
    }

    public String getPersonaCelular() {
        return personaCelular;
    }

    public void setPersonaCelular(String personaCelular) {
        this.personaCelular = personaCelular;
    }

    public String getPersonaTrabajoTelefono() {
        return personaTrabajoTelefono;
    }

    public void setPersonaTrabajoTelefono(String personaTrabajoTelefono) {
        this.personaTrabajoTelefono = personaTrabajoTelefono;
    }

    public String getPersonaTrabajoCelular() {
        return personaTrabajoCelular;
    }

    public void setPersonaTrabajoCelular(String personaTrabajoCelular) {
        this.personaTrabajoCelular = personaTrabajoCelular;
    }

    public String getPersonaCorreoElectronico() {
        return personaCorreoElectronico;
    }

    public void setPersonaCorreoElectronico(String personaCorreoElectronico) {
        this.personaCorreoElectronico = personaCorreoElectronico;
    }

    public String getPersonaEstadoCivil() {
        return personaEstadoCivil;
    }

    public void setPersonaEstadoCivil(String personaEstadoCivil) {
        this.personaEstadoCivil = personaEstadoCivil;
    }

    public String getPersonaProfecioin() {
        return personaProfecioin;
    }

    public void setPersonaProfecioin(String personaProfecioin) {
        this.personaProfecioin = personaProfecioin;
    }

    public String getPersonaNombresApellidosCony() {
        return personaNombresApellidosCony;
    }

    public void setPersonaNombresApellidosCony(String personaNombresApellidosCony) {
        this.personaNombresApellidosCony = personaNombresApellidosCony;
    }

    public String getPersonaNombresApellidosMadr() {
        return personaNombresApellidosMadr;
    }

    public void setPersonaNombresApellidosMadr(String personaNombresApellidosMadr) {
        this.personaNombresApellidosMadr = personaNombresApellidosMadr;
    }

    public String getPersonaNombresApellidosPadr() {
        return personaNombresApellidosPadr;
    }

    public void setPersonaNombresApellidosPadr(String personaNombresApellidosPadr) {
        this.personaNombresApellidosPadr = personaNombresApellidosPadr;
    }

    public String getPersonaNombresApellidosRef1() {
        return personaNombresApellidosRef1;
    }

    public void setPersonaNombresApellidosRef1(String personaNombresApellidosRef1) {
        this.personaNombresApellidosRef1 = personaNombresApellidosRef1;
    }

    public String getPersonaNombresApellidosRef2() {
        return personaNombresApellidosRef2;
    }

    public void setPersonaNombresApellidosRef2(String personaNombresApellidosRef2) {
        this.personaNombresApellidosRef2 = personaNombresApellidosRef2;
    }

    public String getPersonaNombresApellidosRef3() {
        return personaNombresApellidosRef3;
    }

    public void setPersonaNombresApellidosRef3(String personaNombresApellidosRef3) {
        this.personaNombresApellidosRef3 = personaNombresApellidosRef3;
    }

    public Blob getPersonaFirma1() {
        return personaFirma1;
    }

    public void setPersonaFirma1(Blob personaFirma1) {
        this.personaFirma1 = personaFirma1;
    }

    public Blob getPersonaFirma2() {
        return personaFirma2;
    }

    public void setPersonaFirma2(Blob personaFirma2) {
        this.personaFirma2 = personaFirma2;
    }

    public Blob getPersonaFirma3() {
        return personaFirma3;
    }

    public void setPersonaFirma3(Blob personaFirma3) {
        this.personaFirma3 = personaFirma3;
    }


    @Override
    public String toString() {
        return "PersonaModel{" +
                "personaId=" + personaId +
                ", personaNumber='" + personaNumber + '\'' +
                ", personaNombre='" + personaNombre + '\'' +
                ", personaNit='" + personaNit + '\'' +
                ", personaApellidoPaterno='" + personaApellidoPaterno + '\'' +
                ", personaApellidoMaterno='" + personaApellidoMaterno + '\'' +
                ", personaApellidoCazada='" + personaApellidoCazada + '\'' +
                ", personaNombres='" + personaNombres + '\'' +
                ", personaDocumentoIdentidad='" + personaDocumentoIdentidad + '\'' +
                ", personaDocumentoIdentidadNu='" + personaDocumentoIdentidadNu + '\'' +
                ", personaFechaDeNacimiento=" + personaFechaDeNacimiento +
                ", personaLugarDeNacimiento='" + personaLugarDeNacimiento + '\'' +
                ", personaNacionalidad='" + personaNacionalidad + '\'' +
                ", personaDomicilioActual='" + personaDomicilioActual + '\'' +
                ", personaTrabajo='" + personaTrabajo + '\'' +
                ", personaTrabajoCargo='" + personaTrabajoCargo + '\'' +
                ", personaTrabajoDomicilio='" + personaTrabajoDomicilio + '\'' +
                ", personaTelefono='" + personaTelefono + '\'' +
                ", personaCelular='" + personaCelular + '\'' +
                ", personaTrabajoTelefono='" + personaTrabajoTelefono + '\'' +
                ", personaTrabajoCelular='" + personaTrabajoCelular + '\'' +
                ", personaCorreoElectronico='" + personaCorreoElectronico + '\'' +
                ", personaEstadoCivil='" + personaEstadoCivil + '\'' +
                ", personaProfecioin='" + personaProfecioin + '\'' +
                ", personaNombresApellidosCony='" + personaNombresApellidosCony + '\'' +
                ", personaNombresApellidosMadr='" + personaNombresApellidosMadr + '\'' +
                ", personaNombresApellidosPadr='" + personaNombresApellidosPadr + '\'' +
                ", personaNombresApellidosRef1='" + personaNombresApellidosRef1 + '\'' +
                ", personaNombresApellidosRef2='" + personaNombresApellidosRef2 + '\'' +
                ", personaNombresApellidosRef3='" + personaNombresApellidosRef3 + '\'' +
                ", personaFirma1=" + personaFirma1 +
                ", personaFirma2=" + personaFirma2 +
                ", personaFirma3=" + personaFirma3 +
                '}';
    }
}
