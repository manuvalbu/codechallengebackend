package com.sopra.challenge.infrastructure.repository;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.business.port.output.IAccountRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository implements IAccountRepository {

  public Optional<Account> find(String iban) {
    return Optional.empty();
  }

  @Override
  public void save(Account account) {

  }
}
