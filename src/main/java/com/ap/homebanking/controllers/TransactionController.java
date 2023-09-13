package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import com.ap.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                           @RequestParam Double amount, @RequestParam String description,
                                           Authentication authentication) {
        //Aclaraciones:
        //origin account = fromAccountNumber y destiny account = toAccountNumber
        //si los parametros no coinciden en el orden en que fueron enviados igual fciona. Por consistencia los pongo coincidiendo

        Client currentClient = clientService.getClientByEmail(authentication.getName());
        Account debitAccount = accountService.getAccountByNumber(fromAccountNumber);
        Account creditAccount = accountService.getAccountByNumber(toAccountNumber);

        if (fromAccountNumber.isBlank()) {
            return new ResponseEntity<>("Origin account is necessary", HttpStatus.FORBIDDEN);
        } else {
            if (debitAccount == null) {
                return new ResponseEntity<>("Origin account does not exist in accounts", HttpStatus.FORBIDDEN);
            //} else if (currentClient.getAccounts().stream().filter(account -> account.getNumber().equals(fromAccountNumber)).collect(Collectors.toSet()).isEmpty()) {
            } else if (!currentClient.getAccounts().contains(debitAccount)) {    //esta linea hace lo mismo que la de arriba, más resumido
                return new ResponseEntity<>("Account not found in current client", HttpStatus.FORBIDDEN);
            } else {
                if (toAccountNumber.isBlank()) {
                    return new ResponseEntity<>("Destiny account is necessary", HttpStatus.FORBIDDEN);
                } else if (creditAccount == null) {
                    return new ResponseEntity<>("Destiny account does not exist in accounts", HttpStatus.FORBIDDEN);
                } else {
                    if (amount <= 0) {
                        return new ResponseEntity<>("Mount is necessary", HttpStatus.FORBIDDEN);
                    } else if (description.isBlank()) {
                        return new ResponseEntity<>("Description is necessary", HttpStatus.FORBIDDEN);
                    } else if (fromAccountNumber.equals(toAccountNumber)) {
                        return new ResponseEntity<>("Origin and destiny account have to be different", HttpStatus.FORBIDDEN);
                    } else if (debitAccount.getBalance() < amount) {
                        return new ResponseEntity<>("Not enough funds for the transaction", HttpStatus.FORBIDDEN);
                    }
                }
            }
        }
        Transaction transactionDebit = new Transaction(description +" - TO: "+toAccountNumber, (amount * -1), LocalDateTime.now(), TransactionType.DEBIT);
        Transaction transactionCredit = new Transaction(description +" - FROM: "+fromAccountNumber, amount, LocalDateTime.now(), TransactionType.CREDIT);

        debitAccount.setBalance(debitAccount.getBalance() - amount);
        creditAccount.setBalance(creditAccount.getBalance() + amount);

        debitAccount.addTransaction(transactionDebit);
        creditAccount.addTransaction(transactionCredit);

        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);

        return new ResponseEntity<>("Success transaction", HttpStatus.CREATED);
    }
}