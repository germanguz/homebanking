package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.dtos.ClientDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    // task7
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    @RequestMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(accountDTO -> new AccountDTO(accountDTO)).orElse(null);
    }

    // task7
//    @RequestMapping("/clients/current/accounts")
//    public ClientDTO getClientCurrent(Authentication authentication){
//        Client currentClient = clientRepository.findByEmail(authentication.getName());
//        Account newAccount = new Account("VIN-", LocalDate.now(), 0);
//        currentClient.addAccount(newAccount);
//        accountRepository.save(newAccount);
//        return new ClientDTO(currentClient);
//    }
    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        if (currentClient.getAccounts().size() < 3) {
            Account newAccount = new Account("VIN-"+ ((int)(Math.random()*100000000)), LocalDate.now(), 0);
            currentClient.addAccount(newAccount);
            accountRepository.save(newAccount);
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }

        //return new ClientDTO(currentClient);

    }
}
