package com.sopra.challenge.business.port.input;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import java.util.Optional;

public interface ICreateTransaction {

  Double createTransaction(Transaction transaction) throws CreditException;

  Optional<Transaction> searchTransaction(String reference);
}
