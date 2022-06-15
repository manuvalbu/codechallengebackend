package com.sopra.challenge.business.port.input;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import java.util.List;
import java.util.Optional;

public interface ITransactionService {

  Double createTransaction(Transaction transaction) throws CreditException;

  Optional<Transaction> searchTransaction(String reference);

  List<Transaction> searchTransactions(Optional<String> iban, Optional<String> sort);
}
