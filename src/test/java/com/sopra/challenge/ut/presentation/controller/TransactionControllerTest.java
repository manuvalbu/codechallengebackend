package com.sopra.challenge.ut.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sopra.challenge.business.domain.Channel;
import com.sopra.challenge.business.domain.Status;
import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.input.ITransactionService;
import com.sopra.challenge.presentation.controller.TransactionController;
import com.sopra.challenge.presentation.dto.TransactionStatusDTO;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

  @InjectMocks
  TransactionController transactionController;

  @Mock
  ITransactionService transactionServiceMock;

  String reference = "12345A";
  String iban = "ES9820385778983000760236";
  Double amount = 110.5;
  Double fee = 2.5;

  @Test
  void notExistingTransactionOk_UT() {
    //Given
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(Channel.CLIENT.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.empty());
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.INVALID, transactionStatusDTOOutput.getStatus());
  }

  @ParameterizedTest
  @EnumSource(value = Channel.class, names = {"CLIENT", "ATM"})
  void transactionPastClientOrATMOk_UT(Channel channel) {
    //Given
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .dateTime(LocalDateTime.now().minusDays(10))
        .build();
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(channel.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.of(transaction));
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.SETTLED, transactionStatusDTOOutput.getStatus());
    assertEquals(amount - fee, transactionStatusDTOOutput.getAmount());
  }

  @Test
  void transactionPastInternalOk_UT() {
    //Given
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .dateTime(LocalDateTime.now().minusDays(10))
        .build();
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(Channel.INTERNAL.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.of(transaction));
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.SETTLED, transactionStatusDTOOutput.getStatus());
    assertEquals(amount, transactionStatusDTOOutput.getAmount());
    assertEquals(fee, transactionStatusDTOOutput.getFee());
  }

  @ParameterizedTest
  @EnumSource(value = Channel.class, names = {"CLIENT", "ATM"})
  void transactionCurrentClientOrATMOk_UT(Channel channel) {
    //Given
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .dateTime(LocalDateTime.now())
        .build();
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(channel.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.of(transaction));
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.PENDING, transactionStatusDTOOutput.getStatus());
    assertEquals(amount - fee, transactionStatusDTOOutput.getAmount());
  }

  @Test
  void transactionCurrentInternalOk_UT() {
    //Given
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .dateTime(LocalDateTime.now())
        .build();
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(Channel.INTERNAL.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.of(transaction));
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.PENDING, transactionStatusDTOOutput.getStatus());
    assertEquals(amount, transactionStatusDTOOutput.getAmount());
    assertEquals(fee, transactionStatusDTOOutput.getFee());
  }

  @Test
  void transactionFutureClientOk_UT() {
    //Given
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .dateTime(LocalDateTime.now().plusHours(2))
        .build();
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(Channel.CLIENT.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.of(transaction));
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.FUTURE, transactionStatusDTOOutput.getStatus());
    assertEquals(amount - fee, transactionStatusDTOOutput.getAmount());
  }

  @Test
  void transactionFutureATMOk_UT() {
    //Given
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .dateTime(LocalDateTime.now().plusHours(2))
        .build();
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(Channel.ATM.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.of(transaction));
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.PENDING, transactionStatusDTOOutput.getStatus());
    assertEquals(amount - fee, transactionStatusDTOOutput.getAmount());
  }

  @Test
  void transactionFutureInternalOk_UT() {
    //Given
    Transaction transaction = Transaction
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .dateTime(LocalDateTime.now().plusHours(2))
        .build();
    TransactionStatusDTO transactionStatusDTOInput = TransactionStatusDTO
        .builder()
        .channel(Channel.INTERNAL.name())
        .reference(reference)
        .build();
    given(transactionServiceMock.searchTransaction(reference)).willReturn(Optional.of(transaction));
    //When
    TransactionStatusDTO transactionStatusDTOOutput = transactionController.searchTransaction(
        transactionStatusDTOInput).getBody();
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
    assertEquals(reference, transactionStatusDTOOutput.getReference());
    assertEquals(Status.FUTURE, transactionStatusDTOOutput.getStatus());
    assertEquals(amount, transactionStatusDTOOutput.getAmount());
    assertEquals(fee, transactionStatusDTOOutput.getFee());
  }
}
