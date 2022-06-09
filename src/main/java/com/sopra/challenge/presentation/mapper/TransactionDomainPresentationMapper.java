package com.sopra.challenge.presentation.mapper;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.presentation.DTO.TransactionDTO;

public class TransactionDomainPresentationMapper {

  private TransactionDomainPresentationMapper() {}

  public static Transaction toDomainTransaction(TransactionDTO transactionDTO) {
    return Transaction
        .builder()
        .reference(transactionDTO.getReference())
        .iban(transactionDTO.getIban())
        .dateTime(transactionDTO.getDateTime())
        .amount(transactionDTO.getAmount())
        .fee(transactionDTO.getFee())
        .description(transactionDTO.getDescription())
        .build();
  }
}
