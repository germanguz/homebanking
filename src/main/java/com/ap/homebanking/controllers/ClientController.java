package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    // task6
    @Autowired
    private PasswordEncoder passwordEncoder;

    // task7
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    // task6
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password) {
        if(firstName.isEmpty()) {
            return new ResponseEntity<>("First name is necessary",HttpStatus.FORBIDDEN);
        } else if (lastName.isEmpty()) {
            return new ResponseEntity<>("Last name is necessary", HttpStatus.FORBIDDEN);
        } else if (email.isEmpty()) {
            return new ResponseEntity<>("E-mail is necessary", HttpStatus.FORBIDDEN);
        } else if (password.isEmpty()) {
            return new ResponseEntity<>("Password is necessary", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        //task7
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account newAccount = new Account("VIN-"+ ((int)(Math.random()*100000000)), LocalDate.now(), 0);
        newClient.addAccount(newAccount);
        clientRepository.save(newClient);
        accountRepository.save(newAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(clientDTO -> new ClientDTO(clientDTO)).orElse(null);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getClientCurrent(Authentication authentication){
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(currentClient);
    }
}




























