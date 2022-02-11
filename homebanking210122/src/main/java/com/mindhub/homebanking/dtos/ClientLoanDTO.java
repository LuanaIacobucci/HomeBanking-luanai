package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private Long id;
    private Long clientId;
    private Long loanId;
    private String name;
    private double amount;
    private Integer payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.clientId=clientLoan.getClient().getId();
        this.loanId=clientLoan.getLoan().getId();
        this.name=clientLoan.getLoan().getName();
        this.amount=clientLoan.getAmount();
        this.payments=clientLoan.getPayments();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClient_id() {
        return clientId;
    }

    public void setClient_id(Long client_id) {
        this.clientId = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLoan_id() {
        return loanId;
    }

    public void setLoan_id(Long loan_id) {
        this.loanId = loan_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }
}
