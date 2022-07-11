package com.sopra.challenge.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.infrastructure.repository.TransactionRepository;
import com.sopra.challenge.infrastructure.repository.dto.TransactionEntity;
import com.sopra.challenge.infrastructure.repository.persistence.AccountJpaRepository;
import com.sopra.challenge.infrastructure.repository.persistence.TransactionJpaRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepositoryDatabaseTest {

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  TransactionJpaRepository transactionJpaRepository;
  @Autowired
  AccountJpaRepository accountJpaRepository;

  String reference1;
  String reference2;
  String reference3;
  String iban1;
  String iban2;

  @BeforeEach
  void setUp() {
    //get references that don't exist in database
    while (reference1 == null || transactionJpaRepository.findById(reference1).isPresent()) {
      reference1 = UUID.randomUUID().toString();
    }
    while (reference2 == null || transactionJpaRepository.findById(reference2).isPresent()) {
      reference2 = UUID.randomUUID().toString();
    }
    while (reference3 == null || transactionJpaRepository.findById(reference3).isPresent()) {
      reference3 = UUID.randomUUID().toString();
    }
    //get ibans that don't exist in database
    while (iban1 == null || accountJpaRepository.findById(iban1).isPresent()) {
      iban1 = UUID.randomUUID().toString();
    }
    while (iban2 == null || accountJpaRepository.findById(iban2).isPresent()) {
      iban2 = UUID.randomUUID().toString();
    }
  }

  @AfterEach
  void cleanUp() {
    //delete transactions from database if persist
    if (transactionJpaRepository.findById(reference1).isPresent()) {
      transactionJpaRepository.deleteById(reference1);
    }
    if (transactionJpaRepository.findById(reference2).isPresent()) {
      transactionJpaRepository.deleteById(reference2);
    }
    if (transactionJpaRepository.findById(reference3).isPresent()) {
      transactionJpaRepository.deleteById(reference3);
    }
    //delete accounts from database if persist
    if (accountJpaRepository.findById(iban1).isPresent()) {
      accountJpaRepository.deleteById(iban1);
    }
    if (accountJpaRepository.findById(iban2).isPresent()) {
      accountJpaRepository.deleteById(iban2);
    }
  }

  @Test
  void createAndSearchTransactionOk_IT() {
    //Given
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    Transaction newTransaction = Transaction
        .builder()
        .reference(reference1)
        .iban(iban1)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    //When
    transactionRepository.create(newTransaction);
    Optional<Transaction> transactionOptional = transactionRepository.search(reference1);
    //Then
    assertTrue(transactionOptional.isPresent());
    Transaction retrievedTransaction = transactionOptional.get();
    assertEquals(newTransaction, retrievedTransaction);
  }

  @Test
  void searchTransactionsOk_IT() {
    //Given
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    TransactionEntity transaction1 = TransactionEntity
        .builder()
        .reference(reference1)
        .iban(iban1)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction2 = TransactionEntity
        .builder()
        .reference(reference2)
        .iban(iban1)
        .dateTime(date)
        .amount(amount - 50)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction3 = TransactionEntity
        .builder()
        .reference(reference3)
        .iban(iban2)
        .dateTime(date)
        .amount(amount + 100)
        .fee(fee)
        .description(description)
        .build();
    transactionJpaRepository.save(transaction1);
    transactionJpaRepository.save(transaction2);
    transactionJpaRepository.save(transaction3);
    //When
    List<Transaction> transactions = transactionRepository.searchAll(Optional.empty(),
        Optional.empty());
    //Then
    assertEquals(3, transactions.size());
    assertEquals(reference1, transactions.get(0).getReference());
    assertEquals(reference2, transactions.get(1).getReference());
    assertEquals(reference3, transactions.get(2).getReference());
  }

  @Test
  void searchTransactionsAscOk_IT() {
    //Given
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    TransactionEntity transaction1 = TransactionEntity
        .builder()
        .reference(reference1)
        .iban(iban1)
        .dateTime(date)
        .amount(amount + 10)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction2 = TransactionEntity
        .builder()
        .reference(reference2)
        .iban(iban1)
        .dateTime(date)
        .amount(amount - 30)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction3 = TransactionEntity
        .builder()
        .reference(reference3)
        .iban(iban2)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    transactionJpaRepository.save(transaction1);
    transactionJpaRepository.save(transaction2);
    transactionJpaRepository.save(transaction3);
    //When
    List<Transaction> transactions = transactionRepository.searchAll(Optional.empty(),
        Optional.of("asc"));
    //Then
    assertEquals(3, transactions.size());
    assertEquals(transactions.get(0).getAmount(), transactions
        .stream()
        .min(Comparator.comparing(Transaction::getAmount))
        .get()
        .getAmount());
    assertEquals(transactions.get(2).getAmount(), transactions
        .stream()
        .max(Comparator.comparing(Transaction::getAmount))
        .get()
        .getAmount());
  }

  @Test
  void searchTransactionsIbanOk_IT() {
    //Given
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    TransactionEntity transaction1 = TransactionEntity
        .builder()
        .reference(reference1)
        .iban(iban1)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction2 = TransactionEntity
        .builder()
        .reference(reference2)
        .iban(iban1)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction3 = TransactionEntity
        .builder()
        .reference(reference3)
        .iban(iban2)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    transactionJpaRepository.save(transaction1);
    transactionJpaRepository.save(transaction2);
    transactionJpaRepository.save(transaction3);
    //When
    List<Transaction> transactions = transactionRepository.searchAll(Optional.of(iban1),
        Optional.empty());
    //Then
    assertEquals(2, transactions.size());
    assertEquals(iban1, transactions.get(0).getIban());
    assertEquals(iban1, transactions.get(1).getIban());
  }

  @Test
  void searchTransactionsIbanAscSortOk_IT() {
    //Given
    LocalDateTime date = LocalDateTime.now().minusDays(1);
    Double amount = 100.0;
    Double fee = 3.50;
    String description = "Salary income";
    TransactionEntity transaction1 = TransactionEntity
        .builder()
        .reference(reference1)
        .iban(iban1)
        .dateTime(date)
        .amount(amount + 10)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction2 = TransactionEntity
        .builder()
        .reference(reference2)
        .iban(iban1)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    TransactionEntity transaction3 = TransactionEntity
        .builder()
        .reference(reference3)
        .iban(iban2)
        .dateTime(date)
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    transactionJpaRepository.save(transaction1);
    transactionJpaRepository.save(transaction2);
    transactionJpaRepository.save(transaction3);
    //When
    List<Transaction> transactions = transactionRepository.searchAll(Optional.of(iban1),
        Optional.of("asc"));
    //Then
    assertEquals(2, transactions.size());
    assertEquals(iban1, transactions.get(0).getIban());
    assertEquals(iban1, transactions.get(1).getIban());
    assertEquals(transactions.get(0).getAmount(), transactions
        .stream()
        .min(Comparator.comparing(Transaction::getAmount))
        .get()
        .getAmount());
    assertEquals(transactions.get(1).getAmount(), transactions
        .stream()
        .max(Comparator.comparing(Transaction::getAmount))
        .get()
        .getAmount());
  }
}

