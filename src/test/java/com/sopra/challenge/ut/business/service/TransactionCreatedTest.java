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
import com.sopra.challenge.business.service.AccountService;
import com.sopra.challenge.business.service.CreateTransactionService;
import com.sopra.challenge.infrastructure.repository.TransactionRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionCreatedTest {

  @Mock
  AccountService accountServiceMock;
  @Mock
  TransactionRepository transactionRepositoryMock;
  @InjectMocks
  CreateTransactionService createTransactionService;

  @Test
  void positiveTransactionOkTest() throws CreditException, TransactionParameterException {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
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
    Double credit = createTransactionService.createTransaction(transaction);
    //Then
    verify(transactionRepositoryMock, times(1)).save(transaction);
    assertEquals(amount - fee, credit);
  }

  @Test
  void negativeTransactionFailTest() throws TransactionParameterException {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
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
    given(accountServiceMock.retrieveCredit(iban)).willReturn(creditDischarged - 1);
    //Then When
    verify(transactionRepositoryMock, times(0)).save(transaction);
    assertThrows(CreditException.class,
        () -> createTransactionService.createTransaction(transaction));
  }

  @Test
  void negativeTransactionOkTest() throws CreditException, TransactionParameterException {
    //Given
    String reference = "12345A";
    String iban = "ES9820385778983000760236";
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
    Double credit = createTransactionService.createTransaction(transaction);
    //Then
    verify(transactionRepositoryMock, times(1)).save(transaction);
    assertTrue(credit >= 0);
  }
}
