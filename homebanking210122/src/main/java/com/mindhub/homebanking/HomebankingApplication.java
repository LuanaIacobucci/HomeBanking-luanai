package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {


			Client cliente1 = new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("111"));
			Client cliente2 = new Client("Andres", "Oyarzun", "andres@gmail.com",passwordEncoder.encode("112"));
	        clientRepository.save(cliente1);
			clientRepository.save(cliente2);

			//CRECAION DE ADMIN

			Account account1 = new Account("VIN001", LocalDateTime.now(), 500000, cliente1 );
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 375000, cliente1 );
			Account account3 = new Account("VIN003", LocalDateTime.now(), 1500000, cliente2 );
			Account account4 = new Account("VIN004", LocalDateTime.now(), 80000, cliente2 );
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 1000, "Compra de Pan panaderia XXX", LocalDateTime.now(), account1 );
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -55000, "Compra de ropa tienda XXX", LocalDateTime.now(), account1 );
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 7500, "Compra de dulces panaderia XXX", LocalDateTime.now(), account2 );
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -1000, "Compra Supermercado XXX", LocalDateTime.now(), account2 );
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 1000, "Pago  Energia Electrica xxx", LocalDateTime.now(), account2 );
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -1000, "Compra Supermercado XXX", LocalDateTime.now(), account1 );
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, -1000, "Pago Agua xxxx", LocalDateTime.now(), account3 );
			Transaction transaction8 = new Transaction(TransactionType.CREDIT, 1000, "Compra Supermercado XXX", LocalDateTime.now(), account3 );
			Transaction transaction9 = new Transaction(TransactionType.DEBIT, -1000, "Pago gas xxx", LocalDateTime.now(), account3 );
			Transaction transaction10 = new Transaction(TransactionType.DEBIT, -1000, "Compra Supermercado XXX", LocalDateTime.now(), account4 );

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);


			//Tipos de prestamos
			Loan loan1=new Loan("Hipotecario", 500000, Arrays.asList(12,24,36,48,60),LoanType.HIPOTECARIO);
			Loan loan2=new Loan("Personal", 100000, Arrays.asList(6,4,8,10,20),LoanType.PERSONAL);
			Loan loan3=new Loan("Automotriz", 300000, Arrays.asList(12,24,36,48,60),LoanType.AUTOMOTRIZ);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);


			ClientLoan c1=new ClientLoan(cliente1,loan1,4000,60);
			ClientLoan c2=new ClientLoan(cliente1,loan2,50000,12);
			ClientLoan c3=new ClientLoan(cliente2,loan3,20000,36);
			ClientLoan c4=new ClientLoan(cliente2,loan2,1000,6);

			clientLoanRepository.save(c1);
			clientLoanRepository.save(c2);
			clientLoanRepository.save(c3);
			clientLoanRepository.save(c4);

			Card card1=new Card(cliente1.getFirstName()+" "+cliente1.getLastName(),CardType.DEBITO, CardColor.GOLD, "1234-5678-9101-1111",123, LocalDateTime.now().plusYears(5),LocalDateTime.now(),cliente1);
			Card card2=new Card(cliente1.getFirstName()+" "+cliente1.getLastName(),CardType.CREDITO, CardColor.TITANIUM, "1234-5678-9101-1112",124, LocalDateTime.now().plusYears(5),LocalDateTime.now(),cliente1);
			Card card3=new Card(cliente2.getFirstName()+" "+cliente2.getLastName(),CardType.CREDITO, CardColor.SILVER, "1234-5678-9101-1113",125, LocalDateTime.now().plusYears(5),LocalDateTime.now(),cliente2);


			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);


		};
	}
}
