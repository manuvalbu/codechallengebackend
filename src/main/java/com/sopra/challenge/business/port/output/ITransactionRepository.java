package com.sopra.challenge.business.port.output;

import com.sopra.challenge.business.domain.Transaction;
import java.util.List;
import java.util.Optional;

public interface ITransactionRepository {

  void create(Transaction transaction);

  Optional<Transaction> search(String reference);

  List<Transaction> searchAll(Optional<String> iban, Optional<String> sortDir);
}
