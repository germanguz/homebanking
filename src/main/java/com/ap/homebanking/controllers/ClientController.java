package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientController;

    @RequestMapping("/clients")
    public List<Client> getClients() {
        return clientController.findAll();
    }
}
