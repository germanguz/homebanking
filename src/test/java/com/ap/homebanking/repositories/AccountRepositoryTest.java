package com.ap.homebanking.repositories;

import com.ap.homebanking.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    // verifica que exista una cuenta con ese número
    @Test
    public void findByNumber() {
        Account account = accountRepository.findByNumber("VIN001");
        assertThat(account, notNullValue());
    }

    // verifica que no existan cuentas con saldo negativo
    @Test
    public void negativeAccount() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("balance", is(greaterThanOrEqualTo(0.0)))));
    }

    // verifica que los números de cuenta comiencen con "VIN"
    @Test
    public void numberAccount() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", startsWith("VIN"))));
    }

}