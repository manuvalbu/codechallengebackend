package com.sopra.challenge.infrastructure.repository;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.business.port.output.IAccountRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository implements IAccountRepository {

  public Optional<Account> find(String iban) {
    //TODO: implement repository logic
    throw new RuntimeException("Not Implemented");
  }

  @Override
  public void save(Account account) {
    //TODO: implement repository logic
    throw new RuntimeException("Not Implemented");
  }
}
