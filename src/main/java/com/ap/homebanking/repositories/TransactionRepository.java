package com.ap.homebanking.repositories;

import com.ap.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
