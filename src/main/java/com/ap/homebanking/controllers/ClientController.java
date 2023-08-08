package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientController; //dejé este nombre para no confundir con clientRepository de main
    private AccountRepository repo;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientController.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());

//        hace lo mismo que arriba pero mejorado, más profesional
//        return clientController.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientController.findById(id).map(clientDTO -> new ClientDTO(clientDTO)).orElse(null);
    }
}
