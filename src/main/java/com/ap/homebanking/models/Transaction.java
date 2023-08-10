package com.ap.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Transaction {

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    private String description;
    private double amount;
    private LocalDateTime date;
    private TransactionType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;


    // constructores
    public Transaction() {
    }

    public Transaction(String description, double amount, LocalDateTime date, TransactionType type) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.type = type;
    }


    // getters y setters
    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    //@JsonIgnore (no hace falta al tener DTO)
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    // toString
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", type=" + type +
                ", account=" + account +
                '}';
    }
}
