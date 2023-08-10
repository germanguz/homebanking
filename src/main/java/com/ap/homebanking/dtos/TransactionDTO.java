package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import java.time.LocalDateTime;

public class TransactionDTO {

    // atributos
    private long id;
    private String description;
    private double amount;
    private LocalDateTime date;
    private TransactionType type;


    // constructor
    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.type = transaction.getType();
    }


    // getters
    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

}
