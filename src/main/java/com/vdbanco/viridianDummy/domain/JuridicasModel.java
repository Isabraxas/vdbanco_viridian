package com.vdbanco.viridianDummy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Table(name = "JURIDICAS")
@Entity
public class JuridicasModel extends ResourceSupport implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JURIDICAS_SEQ")
    @SequenceGenerator(sequenceName = "JURIDICAS_JURIDICAS_ID_SEQ", allocationSize = 1, name = "JURIDICAS_SEQ")
    private Long juridicasId;

    @NotNull
    private String juridicasNumber;

    private String juridicasNit;
    private String juridicasFundaEmpresa;
    private String juridicasRazonSocial;
    private String juridicasRepresentanteLegal;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "JURIDICAS_REPRESENTANTE_ID")
    @JsonIgnore
    private PersonaModel juridicasRepresentante;

    @NotNull
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

    public PersonaModel getJuridicasRepresentante() {
        return juridicasRepresentante;
    }

    public void setJuridicasRepresentante(PersonaModel juridicasRepresentante) {
        this.juridicasRepresentante = juridicasRepresentante;
    }

    public String getJuridicasRepresentanteLegalNumber() {
        return juridicasRepresentanteLegalNumber;
    }

    public void setJuridicasRepresentanteLegalNumber(String juridicasRepresentanteLegalNumber) {
        this.juridicasRepresentanteLegalNumber = juridicasRepresentanteLegalNumber;
    }
}
