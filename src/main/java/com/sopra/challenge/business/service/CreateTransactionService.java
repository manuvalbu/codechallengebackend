package com.sopra.challenge.business.service;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.port.input.ICreateTranstaction;

public class CreateTransactionService implements ICreateTranstaction {

  @Override
  public Double createTransaction(Transaction transaction) throws CreditException {

    Double amount = transaction.getAmount();
    Double fee = transaction.getFee();

    if (amount - fee < 0) {
      throw new CreditException(amount, fee);
    }

    return transaction.getAmount();
  }
}
