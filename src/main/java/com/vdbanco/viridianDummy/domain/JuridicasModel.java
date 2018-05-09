package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "JURIDICAS")
@Entity
public class JuridicasModel extends ResourceSupport implements Serializable{

    @Id
    private Long juridicasId;
    private String juridicasNumber;
    private String juridicasNit;
    private String juridicasFundaEmpresa;
    private String juridicasRazonSocial;
    private String juridicasRepresentanteLegal;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "JURIDICAS_REPRESENTANTE_ID")
    @JsonIgnore
    private PersonaModel juridicasRepresentanteId;

    @Column(name = "JURIDICAS_REPRESENTANTE_LEGAL_")
    private String juridicasRepresentanteLegalNumber;


    public Long getJuridicasId() {
        return juridicasId;
    }

    public void setJuridicasId(Long juridicasId) {
        this.juridicasId = juridicasId;
    }

    public String getJuridicasNumber() {
        return juridicasNumber;
    }

    public void setJuridicasNumber(String juridicasNumber) {
        this.juridicasNumber = juridicasNumber;
    }

    public String getJuridicasNit() {
        return juridicasNit;
    }

    public void setJuridicasNit(String juridicasNit) {
        this.juridicasNit = juridicasNit;
    }

    public String getJuridicasFundaEmpresa() {
        return juridicasFundaEmpresa;
    }

    public void setJuridicasFundaEmpresa(String juridicasFundaEmpresa) {
        this.juridicasFundaEmpresa = juridicasFundaEmpresa;
    }

    public String getJuridicasRazonSocial() {
        return juridicasRazonSocial;
    }

    public void setJuridicasRazonSocial(String juridicasRazonSocial) {
        this.juridicasRazonSocial = juridicasRazonSocial;
    }

    public String getJuridicasRepresentanteLegal() {
        return juridicasRepresentanteLegal;
    }

    public void setJuridicasRepresentanteLegal(String juridicasRepresentanteLegal) {
        this.juridicasRepresentanteLegal = juridicasRepresentanteLegal;
    }

    public PersonaModel getJuridicasRepresentanteId() {
        return juridicasRepresentanteId;
    }

    public void setJuridicasRepresentanteId(PersonaModel juridicasRepresentanteId) {
        this.juridicasRepresentanteId = juridicasRepresentanteId;
    }

    public String getJuridicasRepresentanteLegalNumber() {
        return juridicasRepresentanteLegalNumber;
    }

    public void setJuridicasRepresentanteLegalNumber(String juridicasRepresentanteLegalNumber) {
        this.juridicasRepresentanteLegalNumber = juridicasRepresentanteLegalNumber;
    }
}
