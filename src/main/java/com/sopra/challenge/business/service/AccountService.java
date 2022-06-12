package com.sopra.challenge.business.service;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.business.port.input.IAccountService;
import com.sopra.challenge.business.port.output.IAccountRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements IAccountService {

  IAccountRepository accountRepository;

  public AccountService(IAccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Double retrieveCredit(String iban) {
    Optional<Account> accountOptional = accountRepository.search(iban);
    if (accountOptional.isEmpty()) {
      Account newAccount = Account
          .builder()
          .amount(0.0)
          .iban(iban)
          .build();
      accountRepository.create(newAccount);
      return newAccount.getAmount();
    }
    return accountOptional.get().getAmount();
  }
}
