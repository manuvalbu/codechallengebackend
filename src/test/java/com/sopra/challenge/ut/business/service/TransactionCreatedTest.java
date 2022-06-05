package com.sopra.challenge.ut.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.input.ICreateTranstaction;
import com.sopra.challenge.business.service.CreateTransactionService;
import org.junit.jupiter.api.Test;


class TransactionCreatedTest {

  ICreateTranstaction createTransactionService = new CreateTransactionService();

  @Test
  void positiveTransactionOkTest() {
    Transaction transaction = new Transaction("12345A", "ES9820385778983000760236",
        "2019-07-16T16:55:42.000Z", 193.38, 3.18, "Restaurant payment");
    Double credit = createTransactionService.createTransaction(transaction);
    assertEquals(193.38, credit);
  }
}
