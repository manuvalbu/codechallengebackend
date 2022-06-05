package com.sopra.challenge.business.service;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.input.ICreateTranstaction;

public class CreateTransactionService implements ICreateTranstaction {

  @Override
  public Double createTransaction(Transaction transaction) {

    return transaction.getAmount();
  }
}
