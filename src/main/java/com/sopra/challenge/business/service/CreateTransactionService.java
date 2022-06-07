package com.sopra.challenge.business.service;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.port.input.ICreateTranstaction;
import com.sopra.challenge.business.port.output.ITransactionRepository;
import com.sopra.challenge.infrastructure.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateTransactionService implements ICreateTranstaction {

  @Autowired
  private final AccountService accountService;

  @Autowired
  private final ITransactionRepository transactionRepository;

  public CreateTransactionService(AccountService accountService,
      TransactionRepository transactionRepository) {
    this.accountService = accountService;
    this.transactionRepository = transactionRepository;
  }

  @Override
  public Double createTransaction(Transaction transaction) throws CreditException {

    Double credit = accountService.retrieveCredit(transaction.getIban());

    Double amount = transaction.getAmount();
    Double fee = transaction.getFee();

    double newCredit = credit + amount - fee;

    if (newCredit < 0) {
      throw new CreditException(amount, fee);
    }

    transactionRepository.save(transaction);

    return newCredit;
  }
}
