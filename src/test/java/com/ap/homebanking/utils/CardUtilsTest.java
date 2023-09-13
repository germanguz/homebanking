package com.ap.homebanking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardUtilsTest {

    @Test
    public void cardNumberIsCreated() {
        String cardNumber = CardUtils.randomNumberCard();
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    void cvvNumberIsCreated() {
        int cvvNumber = CardUtils.randomNumberCvv();
        assertThat(cvvNumber, is(not(0)));
    }
}