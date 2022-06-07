package com.sopra.challenge.business.service;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.infrastructure.repository.AccountRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Double retrieveCredit(String iban) {
    Optional<Account> accountOptional = accountRepository.find(iban);
    if (accountOptional.isEmpty()) {
      Account newAccount = Account
          .builder()
          .amount(0.0)
          .iban(iban)
          .build();
      accountRepository.save(newAccount);
      return newAccount.getAmount();
    }
    return accountOptional.get().getAmount();
  }
}
