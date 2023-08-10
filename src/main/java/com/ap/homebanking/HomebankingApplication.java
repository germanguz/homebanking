package com.ap.homebanking;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {

			// creo 2 clientes
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Daniel", "Guira", "daniel@mindhub.com");

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

		};
	}

}
