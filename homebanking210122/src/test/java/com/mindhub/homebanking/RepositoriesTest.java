package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest

@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {





        @Autowired

        LoanRepository loanRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;





    @Test

        public void existLoans(){

            List<Loan> loans = loanRepository.findAll();

            assertThat(loans,is(not(empty())));

        }



        @Test

        public void existPersonalLoan(){

            List<Loan> loans = loanRepository.findAll();

            assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

        }



    @Test

    public void existClient(){

        List<Client> client = clientRepository.findAll();

        assertThat(client,is(not(empty())));

    }

    @Test

    public void existTransaction(){

        List<Transaction> tr = transactionRepository.findAll();

        assertThat(tr,is(not(empty())));

    }


    @Test

    public void existAccounts(){

        List<Account> acc = accountRepository.findAll();

        assertThat(acc,is(not(empty())));

    }

    @Test

    public void existCards(){

        List<Card> card = cardRepository.findAll();

        assertThat(card,(not(empty())));

    }



}



