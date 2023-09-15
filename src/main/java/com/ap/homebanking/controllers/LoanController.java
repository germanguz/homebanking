package com.ap.homebanking.controllers;

import com.ap.homebanking.dtos.ClientLoanDTO;
import com.ap.homebanking.dtos.LoanApplicationDTO;
import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import com.ap.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;


    @Transactional
    @PostMapping("/loans")
    // Debe recibir un objeto de solicitud de crédito con los datos del préstamo
    public ResponseEntity<Object> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        Loan currentLoan = loanService.getLoanById(loanApplicationDTO.getLoanId());
        Account destinyAccount = accountService.getAccountByNumber(loanApplicationDTO.getToAccountNumber());
        Client currentClient = clientService.getClientByEmail(authentication.getName());

        // Que el monto no sea 0
        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Amount must be greater than zero", HttpStatus.FORBIDDEN);
        }
        // Que las cuotas no sean 0 (no entra nunca porque cae primero )
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Amount of payments forbidden", HttpStatus.FORBIDDEN);
        }
        // Verificar que el préstamo exista
        if (currentLoan == null) {
            return new ResponseEntity<>("Loan type not found", HttpStatus.FORBIDDEN);
        }
        // Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if (!currentLoan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("amount of payments not available", HttpStatus.FORBIDDEN);
        }
        // Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if (loanApplicationDTO.getAmount() > currentLoan.getMaxAmount()) {
            return new ResponseEntity<>("maximum loan amount allowed exceeded", HttpStatus.FORBIDDEN);
        }
        // Verificar que la cuenta de destino exista
        if (destinyAccount == null) {
            return new ResponseEntity<>("Destiny account does not exist in accounts", HttpStatus.FORBIDDEN);
        }
        // Verificar que la cuenta de destino pertenezca al cliente autenticado
        if (!currentClient.getAccounts().contains(destinyAccount)) {
            return new ResponseEntity<>("Account not found in current client", HttpStatus.FORBIDDEN);
        }

        // Solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.2, loanApplicationDTO.getPayments());

        // Crear transacción “CREDIT” asociada a la cuenta de destino, con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        Transaction transaction = new Transaction(currentLoan.getName() + " - loan approved",
                                loanApplicationDTO.getAmount(), LocalDateTime.now(),TransactionType.CREDIT);

        // Actualizar la cuenta de destino sumando el monto solicitado
        destinyAccount.setBalance(destinyAccount.getBalance() + loanApplicationDTO.getAmount());

        // agrego la transacción a la cta con la que estoy trabajando
        destinyAccount.addTransaction(transaction);
        // agrego el tipo de préstamo al préstamo (loan_id)
        currentLoan.addClientLoan(clientLoan);
        // agrego el cliente al préstamo solicitado (client_id)
        currentClient.addClientLoan(clientLoan);

        transactionService.saveTransaction(transaction);
        clientLoanService.saveClientLoan(clientLoan);

        // para mostrar el JSON porque si usaba clientLoan me daba recursividad
        ClientLoanDTO clientLoanDTO = new ClientLoanDTO(clientLoan);

        //return new ResponseEntity<>("Approved loan", HttpStatus.CREATED);  //para mostrar un mensaje
        return new ResponseEntity<>(clientLoanDTO, HttpStatus.CREATED);  //para mostrar JSON resultante
    }

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }
}