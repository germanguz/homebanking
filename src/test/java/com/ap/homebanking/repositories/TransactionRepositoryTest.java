package com.ap.homebanking.repositories;

import com.ap.homebanking.models.Transaction;
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
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    // verifica que cada objeto transaction sea instacia de Transaction
    @Test
    public void instanceOfTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction: transactions) {
            assertThat(transaction, instanceOf(Transaction.class));
        }
    }

    // verifica que la transacción tenga una cuenta asignada
    @Test
    public void existAccountInTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("account", notNullValue())));
    }

}