package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.LoanType;

public class LoanApplicationDTO {
    private long loanId;

    private double amount;

    private int payments;
    private String toAccountNumber;
    private LoanType type;

    public LoanApplicationDTO(long loanId, double amount, int payments, String toAccountNumber, LoanType type) {
        this.loanId = loanId;
        this.amount=amount;
        this.payments=payments;
        this.toAccountNumber=toAccountNumber;
        this.type=type;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }
}
