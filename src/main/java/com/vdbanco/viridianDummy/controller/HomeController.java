package com.vdbanco.viridianDummy.controller;

import com.vdbanco.viridianDummy.domain.ResourcesModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController

public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "redirect:/api";
    }

    @RequestMapping("/api")
    public ResourcesModel apiHome() {

        ResourcesModel resources = new ResourcesModel();

        Link linkUsers = ControllerLinkBuilder.linkTo(methodOn(UserController.class).getAllPageable(0,20)).withRel("Users");
        resources.setUsers(linkUsers.getHref());

        Link linkEmpleados = ControllerLinkBuilder.linkTo(methodOn(EmpleadoController.class).getAllPageable(0,20)).withRel("Empleados");
        resources.setEmpleados(linkEmpleados.getHref());

        Link linkPersonas = ControllerLinkBuilder.linkTo(methodOn(PersonaController.class).getAllPageable(0,20)).withRel("Personas");
        resources.setPersonas(linkPersonas.getHref());

        Link linkJuridicas = ControllerLinkBuilder.linkTo(methodOn(JuridicasController.class).getAllPageable(0,20)).withRel("Juridicas");
        resources.setJuridicas(linkJuridicas.getHref());

        Link linkAccounts = ControllerLinkBuilder.linkTo(methodOn(AccountController.class).getAllPageable(0,20)).withRel("Accounts");
        resources.setAccounts(linkAccounts.getHref());

        Link linkProductosBancarios = ControllerLinkBuilder.linkTo(methodOn(ProductosBancariosController.class).getAllPageable(0,20)).withRel("ProductosBancarios");
        resources.setProductosBancarios(linkProductosBancarios.getHref());

        Link linkAccountHolders = ControllerLinkBuilder.linkTo(methodOn(AccountHolderController.class).getAllPageable(0,20)).withRel("AccountHolders");
        resources.setAccountHolders(linkAccountHolders.getHref());

        Link linkAutorizaciones = ControllerLinkBuilder.linkTo(methodOn(AutorizacionController.class).getAllPageable(0,20)).withRel("Autorizaciones");
        resources.setAutorizaciones(linkAutorizaciones.getHref());

        Link linkTransacciones = ControllerLinkBuilder.linkTo(methodOn(TransaccionController.class).getAllPageable(0,20)).withRel("transacciones");
        resources.setTransacciones(linkTransacciones.getHref());

        return resources;
    }

}
