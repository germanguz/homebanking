package com.ap.homebanking.repositories;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Loan;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

//import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    // task11
    // account
    @Test
    public void negativeAccount() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("balance", is(greaterThanOrEqualTo(0.0)))));
    }

    @Test
    public void numberAccount() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", startsWith("VIN"))));
    }

    // client
    @Test
    public void notEmptyList() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void mailNotNull() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            assertThat(client.getEmail(), notNullValue());
        }
//        for (int i = 0; i < clients.size(); i++) {
//            String email = clients.get(i).getEmail();
//            assertThat(email, notNullValue());
//        }
    }
}
