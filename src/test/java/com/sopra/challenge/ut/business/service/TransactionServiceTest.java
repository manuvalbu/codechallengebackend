package com.sopra.challenge.ut.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.exception.TransactionParameterException;
import com.sopra.challenge.business.port.input.IAccountService;
import com.sopra.challenge.business.port.output.ITransactionRepository;
import com.sopra.challenge.business.service.TransactionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock
  IAccountService accountServiceMock;
  @Mock
  ITransactionRepository transactionRepositoryMock;
  @InjectMocks
  TransactionService transactionService;

  String reference = "12345A";
  String iban = "ES9820385778983000760236";

  @Test
  void createPositiveTransactionOk_UT() throws CreditException, TransactionParameterException {
    //Given
    Double amount = 1000.0;
    Double fee = 3.50;
    String description = "Salary income";
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(amount)
        .fee(fee)
        .description(description)
        .build();
    given(accountServiceMock.retrieveCredit(iban)).willReturn(0.0);
    //When
    Double credit = transactionService.createTransaction(transaction);
    //Then
    verify(transactionRepositoryMock, times(1)).create(transaction);
    assertEquals(amount - fee, credit);
  }

  @Test
  void createNegativeTransactionFails_UT() throws TransactionParameterException {
    //Given
    Double amount = -100.0;
    Double fee = 3.50;
    double creditDischarged = (amount - fee) * -1;
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
    given(accountServiceMock.retrieveCredit(iban)).willReturn(creditDischarged - 1);
    //Then When
    verify(transactionRepositoryMock, times(0)).create(transaction);
    assertThrows(CreditException.class,
        () -> transactionService.createTransaction(transaction));
  }

  @Test
  void createNegativeTransactionOk_UT() throws CreditException, TransactionParameterException {
    //Given
    Double amount = -100.0;
    Double fee = 3.50;
    Double creditDischarged = (amount - fee) * -1;
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
    given(accountServiceMock.retrieveCredit(iban)).willReturn(creditDischarged);
    //When
    Double credit = transactionService.createTransaction(transaction);
    //Then
    verify(transactionRepositoryMock, times(1)).create(transaction);
    assertTrue(credit >= 0);
  }

  @Test
  void searchTransactionsOk_UT() {
    //When
    List<Transaction> transactions = transactionService.searchTransactions(Optional.empty(),
        Optional.empty());
    //Then
    verify(transactionRepositoryMock, times(1)).searchAll(Optional.empty(), Optional.empty());
  }
}
