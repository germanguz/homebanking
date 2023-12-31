package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.services.AccountService;
import com.ap.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccount(id);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (currentClient.getAccounts().size() < 3) {
            Account newAccount = new Account("VIN-"+ ((int)(Math.random()*100000000)), LocalDate.now(), 0);
            currentClient.addAccount(newAccount);
            accountService.saveAccount(newAccount);
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsCurrentClient(Authentication authentication) {
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        return currentClient.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }
}
