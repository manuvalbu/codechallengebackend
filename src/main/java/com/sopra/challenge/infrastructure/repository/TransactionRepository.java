package com.sopra.challenge.infrastructure.repository;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.output.ITransactionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository implements ITransactionRepository {

  @Override
  public void save(Transaction transaction) {

  }
}
