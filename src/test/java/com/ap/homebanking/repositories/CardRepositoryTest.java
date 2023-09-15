package com.ap.homebanking.repositories;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardType;
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
class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;

    // verfica que en tarjetas exista el tipo CREDIT
    @Test
    public void typeNotNull() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));
    }

    // verifica que el número cvv sea menor que 1000
    @Test
    public void cvvNumber() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cvv", is(lessThan(1000)))));
    }

}