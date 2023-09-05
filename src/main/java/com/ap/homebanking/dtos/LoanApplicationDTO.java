package com.ap.homebanking.dtos;

public class LoanApplicationDTO {

    // atributos
    private long loanId;
    private double amount;
    private int payments;
    private String toAccountNumber;

    // getters
    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}
