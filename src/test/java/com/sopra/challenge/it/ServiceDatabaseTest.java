package com.sopra.challenge.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.exception.TransactionParameterException;
import com.sopra.challenge.business.service.TransactionService;
import com.sopra.challenge.infrastructure.repository.persistence.AccountJpaRepository;
import com.sopra.challenge.infrastructure.repository.persistence.TransactionJpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceDatabaseTest {

  @Autowired
  TransactionService transactionService;

  @Autowired
  TransactionJpaRepository transactionJpaRepository;
  @Autowired
  AccountJpaRepository accountJpaRepository;

  String reference;
  String iban;

  @BeforeEach
  void setUp() {
    //get reference that don't exist in database
    while (reference == null || transactionJpaRepository.findById(reference).isPresent()) {
      reference = UUID.randomUUID().toString();
    }
    //get iban that don't exist in database
    while (iban == null || accountJpaRepository.findById(iban).isPresent()) {
      iban = UUID.randomUUID().toString();
    }
  }

  @AfterEach
  void cleanUp() {
    //delete transaction from database if persists
    if (transactionJpaRepository.findById(reference).isPresent()) {
      transactionJpaRepository.deleteById(reference);
    }
    //delete account from database if persists
    if (accountJpaRepository.findById(iban).isPresent()) {
      accountJpaRepository.deleteById(iban);
    }
  }

  @Test
  void createTransactionOk_IT() {
    //Given
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
    //When
    transactionService.createTransaction(newTransaction);
    Optional<Transaction> transactionOptional = transactionService.searchTransaction(
        reference);
    //Then
    assertTrue(transactionOptional.isPresent());
    Transaction retrievedTransaction = transactionOptional.get();
    assertEquals(newTransaction, retrievedTransaction);
  }

  @Test
  void createTransactionCreditFails_IT() {
    //Given
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = -1000.0;
    Double fee = 3.50;
    String description = "Big payment";
    Transaction newTransaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    //Then When
    assertThrows(CreditException.class,
        () -> transactionService.createTransaction(newTransaction));
    //Then
    Optional<Transaction> transactionOptional = transactionService.searchTransaction(
        reference);
    assertTrue(transactionOptional.isEmpty());
  }

  @Test
  void createTransaction0AmountFails_IT() {
    //Given
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = 0.0;
    Double fee = 3.50;
    String description = "Nothing";
    //Then When
    TransactionParameterException transactionParameterException = assertThrows(
        TransactionParameterException.class,
        () -> {
          Transaction newTransaction = Transaction
              .builder()
              .reference(reference)
              .iban(iban)
              .dateTime(date)
              .amount(amount)
              .fee(fee)
              .description(description)
              .build();
          transactionService.createTransaction(newTransaction);
        });
    //Then
    assertTrue(transactionParameterException.getMessage().contains("amount"));
    Optional<Transaction> transactionOptional = transactionService.searchTransaction(
        reference);
    assertTrue(transactionOptional.isEmpty());
  }
}

