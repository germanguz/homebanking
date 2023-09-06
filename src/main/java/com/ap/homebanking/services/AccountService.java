package com.ap.homebanking.services;

import com.ap.homebanking.dtos.AccountDTO;
import com.ap.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts();

    AccountDTO getAccount(Long id);

    void saveAccount(Account account);

    Account getAccountByNumber(String number);
}
