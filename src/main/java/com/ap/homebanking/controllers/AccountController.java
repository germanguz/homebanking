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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

//    @Autowired
//    private AccountRepository accountRepository;
    // task7
//    @Autowired
//    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        //return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
        return accountService.getAccounts();
    }

    @RequestMapping("accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        //return accountRepository.findById(id).map(accountDTO -> new AccountDTO(accountDTO)).orElse(null);
        return accountService.getAccount(id);
    }

    // task7
    @RequestMapping(value = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication){
        //Client currentClient = clientRepository.findByEmail(authentication.getName());
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        if (currentClient.getAccounts().size() < 3) {
            Account newAccount = new Account("VIN-"+ ((int)(Math.random()*100000000)), LocalDate.now(), 0);
            currentClient.addAccount(newAccount);
            //accountRepository.save(newAccount);
            accountService.saveAccount(newAccount);
            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Account limit reached", HttpStatus.FORBIDDEN);
        }
    }

    // task8
    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsCurrentClient(Authentication authentication) {
        //Client currentClient = clientRepository.findByEmail(authentication.getName());
        Client currentClient = clientService.getClientByEmail(authentication.getName());
        return currentClient.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }
}
