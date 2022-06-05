package com.sopra.challenge.ut.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.port.input.ICreateTranstaction;
import com.sopra.challenge.business.service.CreateTransactionService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;


class TransactionCreatedTest {

  ICreateTranstaction createTransactionService = new CreateTransactionService();

  @Test
  void positiveTransactionOkTest() throws CreditException {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Restaurant payment";
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(LocalDateTime.now())
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    //When
    Double credit = createTransactionService.createTransaction(transaction);
    //Then
    assertEquals(amount, credit);
  }

  @Test
  void negativeTransactionFailTest() {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    Double amount = -100.0;
    Double fee = 3.50;
    String description = "Restaurant payment";
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    //Then When
    assertThrows(CreditException.class,
        () -> createTransactionService.createTransaction(transaction));
  }
}
