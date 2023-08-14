package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.ClientLoan;
import com.ap.homebanking.models.Loan;

import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toList;

public class ClientDTO {

    // atributos
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;

    //task4 dudas...
//    private List<ClientLoan> loans;
    private Set<ClientLoanDTO> loans;


    // constructor
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet());
        this.loans = client.getLoans().stream().map(clientLoan2 -> new ClientLoanDTO(clientLoan2)).collect(toSet());
        //this.loans = clientLoan.getLoan().stream().map(clientLoan2 -> new ClientLoanDTO(clientLoan2)).collect(toSet());
        //this.loans = client.getLoans();//.map(sub -> sub.getLoan()).collect(toList());
        //this.loans = client.getLoans().stream().map(clientLoan2 -> new ClientLoanDTO(clientLoan2)).collect(toList());
    }


    // getters
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }
}
