package com.ap.homebanking;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

			// creo 2 clientes
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("1234"));
			Client client2 = new Client("Daniel", "Guira", "daniel@mindhub.com", passwordEncoder.encode("1234"));

			// creo 4 cuentas, 2 para cada cliente
			Account account1 = new Account("VIN001", LocalDate.now(), 5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			Account account3 = new Account("VIN003", LocalDate.now(), 2500);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(1), 8000);

			// asigno las cuentas a los clientes
			client1.addAccount(account1);
			client1.addAccount(account2);

			client2.addAccount(account3);
			client2.addAccount(account4);

			//task3
			// creo 4 transacciones, 2 para cada cuenta del cliente1
			Transaction transaction1 = new Transaction("deposit", 2000, LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction2 = new Transaction("pay", 500, LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transaction3 = new Transaction("donation", 3500, LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction4 = new Transaction("transfer", 1500, LocalDateTime.now().plusHours(1), TransactionType.DEBIT);

			// asigno 2 transacciones a cada una de las 2 primeras cuentas, que son del cliente1
			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);

			// creo 4 transacciones, 2 para cada cuenta, pero ahora del cliente2
			Transaction transaction5 = new Transaction("deposit cash", 20000, LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction6 = new Transaction("pay bill", 1800, LocalDateTime.now().plusDays(2), TransactionType.DEBIT);
			Transaction transaction7 = new Transaction("service", 4500, LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction8 = new Transaction("direct transfer", 2500, LocalDateTime.now().plusHours(2), TransactionType.DEBIT);

			// asigno 2 transacciones a cada una de las 2 segundas cuentas, que son del cliente2
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account4.addTransaction(transaction7);
			account4.addTransaction(transaction8);

			// guardo el cliente (si no lo guardo no existe el id para que use la cuenta, entonces falla)
			clientRepository.save(client1);
			clientRepository.save(client2);

			// guardo las cuentas usando el id de cliente y, a su vez genero el id de cuenta que necesita la transacción
			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			// guardo la transacción usando el id de cuenta
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			//task4
			// creo 3 tipos de prestamos
			Loan loan1 = new Loan("Hipotecario", 500000, List.of(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000, List.of(6, 12, 24));
			Loan loan3 = new Loan("Automotriz", 300000, List.of(6, 12, 24, 36));

			// guardo los prestamos en el repositorio de prestamos
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			// creo 4 prestamos, 2 para cada cliente
			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			// asigno los prestamos a tipo de prestamo y cliente para guardar en clientLoan
			// tipo de prestamo 1 y cliente 1
			loan1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan1);
			// tipo de prestamo 2 y cliente 1
			loan2.addClientLoan(clientLoan2);
			client1.addClientLoan(clientLoan2);
			// tipo de prestamo 2 y cliente 2
			loan2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan3);
			// tipo de prestamo 3 y cliente 2
			loan3.addClientLoan(clientLoan4);
			client2.addClientLoan(clientLoan4);

			// guardo los prestamos en la BD de clientLoan
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			// task5
			// creo 2 tarjetas para el cliente 1
			Card card1 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.DEBIT, CardColorType.GOLD,"1111-2222-3333-4444", 123, LocalDate.now(), LocalDate.now().plusYears(5));
			Card card2 = new Card(client1.getFirstName()+" "+client1.getLastName(),CardType.CREDIT, CardColorType.TITANIUM,"5555-6666-7777-8888", 456, LocalDate.now(), LocalDate.now().plusYears(5));

			// creo 1 tarjeta para el cliente 2
			Card card3 = new Card(client2.getFirstName()+" "+client2.getLastName(),CardType.CREDIT, CardColorType.SILVER,"1234-5678-9101-1123", 357, LocalDate.now(), LocalDate.now().plusYears(5));

			// asigno 2 tarjetas al cliente 1, y 1 al cliente 2
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			// guardo las tarjetas
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

			// task6
			Client admin = new Client("admin", "admin", "admin@mindhub.com", passwordEncoder.encode("admin"));
			clientRepository.save(admin);


		};
	}

}























