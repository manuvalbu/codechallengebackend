package com.sopra.challenge.business.port.output;

import com.sopra.challenge.business.domain.Transaction;
import java.util.Optional;

public interface ITransactionRepository {

  void create(Transaction transaction);

  Optional<Transaction> search(String reference);
}
