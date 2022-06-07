package com.sopra.challenge.business.port.output;

import com.sopra.challenge.business.domain.Transaction;

public interface ITransactionRepository {

  void save(Transaction transaction);
}
