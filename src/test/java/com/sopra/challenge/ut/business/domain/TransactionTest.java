package com.sopra.challenge.ut.business.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.TransactionParameterException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TransactionTest {

  @Test
  void transactionOkTest_UT() {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    //When
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    //Then
    assertEquals(iban, transaction.getIban());
    assertEquals(amount, transaction.getAmount());
  }

  @Test
  void transactionNoIbanFails_UT() {
    //Given
    String reference = "12345A";
    String iban = null;
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    //Then When
    TransactionParameterException transactionParameterException = assertThrows(
        TransactionParameterException.class,
        () -> Transaction
            .builder()
            .reference(reference)
            .iban(iban)
            .dateTime(LocalDateTime.now().minusDays(1))
            .amount(amount)
            .fee(fee)
            .description(description)
            .build());
    assertTrue(transactionParameterException.getMessage().contains("IBAN"));
  }

  @Test
  void transactionNoAmountFails_UT() {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    Double amount = null;
    Double fee = 3.50;
    String description = "Salary income";
    //Then When
    TransactionParameterException transactionParameterException = assertThrows(
        TransactionParameterException.class,
        () -> Transaction
            .builder()
            .reference(reference)
            .iban(iban)
            .dateTime(LocalDateTime.now().minusDays(1))
            .amount(amount)
            .fee(fee)
            .description(description)
            .build());
    assertTrue(transactionParameterException.getMessage().contains("amount"));
  }

  @Test
  void transactionBlankIbanFails_UT() {
    //Given
    String reference = "12345A";
    String iban = "  ";
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    //Then When
    TransactionParameterException transactionParameterException = assertThrows(
        TransactionParameterException.class,
        () -> Transaction
            .builder()
            .reference(reference)
            .iban(iban)
            .dateTime(LocalDateTime.now().minusDays(1))
            .amount(amount)
            .fee(fee)
            .description(description)
            .build());
    assertTrue(transactionParameterException.getMessage().contains("IBAN"));
  }

  @Test
  void transaction0AmountFails_UT() {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    Double amount = 0.0;
    Double fee = 3.50;
    String description = "Salary income";
    //Then When
    TransactionParameterException transactionParameterException = assertThrows(
        TransactionParameterException.class,
        () -> Transaction
            .builder()
            .reference(reference)
            .iban(iban)
            .dateTime(LocalDateTime.now().minusDays(1))
            .amount(amount)
            .fee(fee)
            .description(description)
            .build());
    assertTrue(transactionParameterException.getMessage().contains("amount"));
  }
}
