package com.sopra.challenge.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.service.CreateTransactionService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceDatabaseTest {

  @Autowired
  CreateTransactionService createTransactionService;

  @Test
  void createTransactionOk() {
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    Transaction newTransaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    createTransactionService.createTransaction(newTransaction);
    Optional<Transaction> transactionOptional = createTransactionService.searchTransaction(
        reference);
    assertTrue(transactionOptional.isPresent());
    Transaction retrievedTransaction = transactionOptional.get();
    assertEquals(newTransaction, retrievedTransaction);
  }

  @Test
  void createTransactionCreditFails() {
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = -1000.0;
    Double fee = 3.50;
    String description = "Payment";
    Transaction newTransaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    assertThrows(CreditException.class,
        () -> createTransactionService.createTransaction(newTransaction));
    Optional<Transaction> transactionOptional = createTransactionService.searchTransaction(
        reference);
    assertTrue(transactionOptional.isEmpty());
  }
}

