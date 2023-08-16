package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColorType;
import com.ap.homebanking.models.CardType;
import java.time.LocalDate;

public class CardDTO {

    // atributos
    private long id;
    private String cardHolder;
    private CardType type;
    private CardColorType color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    //private Client client; si lo uso genera recursividad, ya tengo para esto el cardHolder

    // constructor
    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }

    // getters
    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public CardColorType getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
