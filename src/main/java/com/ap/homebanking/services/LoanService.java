package com.ap.homebanking.services;

import com.ap.homebanking.dtos.LoanDTO;
import com.ap.homebanking.models.Loan;

import java.util.List;

public interface LoanService {

    Loan getLoanById(Long id);

    List<LoanDTO> getLoans();
}
