package com.ap.homebanking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardUtilsTest {

    // verifica que se cree un número de tarjeta
    @Test
    public void cardNumberIsCreated() {
        String cardNumber = CardUtils.randomNumberCard();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    // verifica que el número creado sea de tipo String
    @Test
    public void cardNumberIsString() {
        String cardNumber = CardUtils.randomNumberCard();
        assertThat(cardNumber, instanceOf(String.class));
    }

    // verifica que el cvv creado no sea cero
    @Test
    void cvvNumberIsCreated() {
        int cvvNumber = CardUtils.randomNumberCvv();
        assertThat(cvvNumber, is(not(0)));
    }

    // verifica que el número cvv esté en 100 y 999
    @Test
    public void cvvNumberBetween() {
        int cvvNumber = CardUtils.randomNumberCvv();
        assertThat(cvvNumber, either(greaterThan(99)).or(lessThan(1000)));
    }

}