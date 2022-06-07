package com.sopra.challenge.business.port.output;

import com.sopra.challenge.business.domain.Account;
import java.util.Optional;

public interface IAccountRepository {

  Optional<Account> find(String iban);

  void save(Account account);
}
