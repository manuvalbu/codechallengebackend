package com.sopra.challenge.ut.presentation.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sopra.challenge.business.domain.Channel;
import com.sopra.challenge.business.domain.Status;
import com.sopra.challenge.business.port.input.ITransactionService;
import com.sopra.challenge.presentation.controller.TransactionController;
import com.sopra.challenge.presentation.dto.TransactionStatusDTO;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

}
