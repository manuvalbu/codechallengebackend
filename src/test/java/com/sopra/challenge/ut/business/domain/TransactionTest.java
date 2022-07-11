package com.sopra.challenge.ut.business.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.TransactionParameterException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TransactionTest {

  @Test
  void transactionOk_UT() {
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
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    //Then When
    TransactionParameterException transactionParameterException = assertThrows(
        TransactionParameterException.class,
        () -> Transaction
            .builder()
            .reference(reference)
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

  @Test
  void transactionNoReferenceOk_UT() {
    //Given
    String iban = "ES9820385778983000760236";
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    //When
    Transaction transaction = Transaction
        .builder()
        .iban(iban)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    //Then
    assertNotNull(transaction.getReference());
    assertFalse(transaction.getReference().isBlank());
  }

  @Test
  void transactionNoFeeOk_UT() {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    Double amount = 100.0;
    String description = "Salary income";
    //When
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(amount)
        .description(description)
        .build();
    //Then
    assertNotNull(transaction.getFee());
    assertEquals(0.0, transaction.getFee());
  }

  @Test
  void transactionNoDateTimeOk_UT() {
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
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    //Then
    assertNotNull(transaction.getDateTime());
    assertTrue(LocalDateTime.now().isAfter(transaction.getDateTime()));
  }
}
