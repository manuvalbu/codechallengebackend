package com.sopra.challenge.business.port.output;

import com.sopra.challenge.business.domain.Account;
import java.util.Optional;

public interface IAccountRepository {

  Optional<Account> search(String iban);

  void create(Account account);
}
