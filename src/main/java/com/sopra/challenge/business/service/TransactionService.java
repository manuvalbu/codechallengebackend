package com.sopra.challenge.business.service;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.port.input.IAccountService;
import com.sopra.challenge.business.port.input.ITransactionService;
import com.sopra.challenge.business.port.output.ITransactionRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {

  private final IAccountService accountService;

  private final ITransactionRepository transactionRepository;

  public TransactionService(IAccountService accountService,
      ITransactionRepository transactionRepository) {
    this.accountService = accountService;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Double createTransaction(Transaction transaction) {

    Double credit = accountService.retrieveCredit(transaction.getIban());

    Double amount = transaction.getAmount();
    Double fee = transaction.getFee();

    double newCredit = credit + amount - fee;

    if (newCredit < 0) {
      throw new CreditException(amount, fee);
    }

    transactionRepository.create(transaction);

    return newCredit;
  }

  @Override
  public Optional<Transaction> searchTransaction(String reference) {
    return transactionRepository.search(reference);
  }

  @Override
  public List<Transaction> searchTransactions(Optional<String> iban, Optional<String> sortDir) {
    return transactionRepository.searchAll(iban, sortDir);
  }
}
