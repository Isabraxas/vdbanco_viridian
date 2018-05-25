package com.vdbanco.viridianDummy.funciones;

import com.vdbanco.viridianDummy.domain.AccountHolderModel;
import com.vdbanco.viridianDummy.domain.AccountModel;
import com.vdbanco.viridianDummy.domain.PersonaModel;
import com.vdbanco.viridianDummy.domain.UserModel;
import com.vdbanco.viridianDummy.services.AccountHolderService;
import com.vdbanco.viridianDummy.services.AccountService;
import com.vdbanco.viridianDummy.services.PersonaService;
import com.vdbanco.viridianDummy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductosClienteServiceImpl implements ProductosClienteService{

    private UserService userService;
    private PersonaService personaService;
    private AccountService accountService;
    private AccountHolderService accountHolderService;

    @Autowired
    public ProductosClienteServiceImpl(UserService userService, PersonaService personaService,
                                       AccountService accountService ,AccountHolderService accountHolderService) {
        this.userService = userService;
        this.personaService = personaService;
        this.accountService = accountService;
        this.accountHolderService = accountHolderService;
    }


    @Override
    public ProductosClienteModel getProductosByUser(String username) {
        ProductosClienteModel productosClienteModel = new ProductosClienteModel();

        UserModel userModel = userService.getByUserName(username);
        //PersonaModel personaModel = personaService.getByPersonaNumber(userModel.get().getPersonaPersonaNumber());
        PersonaModel personaModel = userModel.getPersona();
        AccountHolderModel accountHolderModel = accountHolderService.getAccountHolderByPersonaNumber(personaModel.getPersonaNumber());
        List<AccountModel> accountModel = accountService.getAccountByAccountHolder(accountHolderModel.getAccountHolderNumber());


        productosClienteModel.setUserId(userModel.getUserId());
        productosClienteModel.setPersonaPersonaNumber(userModel.getPersona().getPersonaNumber());
        productosClienteModel.setPersona(personaModel);
        productosClienteModel.setCuentas(accountModel);

        return productosClienteModel;
    }
}
