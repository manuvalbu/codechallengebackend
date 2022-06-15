package com.sopra.challenge.presentation.mapper;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.presentation.dto.TransactionDTO;

public class TransactionDomainPresentationMapper {

  private TransactionDomainPresentationMapper() {
  }

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

  public static TransactionDTO toPresentationTransaction(Transaction transaction) {
    return TransactionDTO
        .builder()
        .reference(transaction.getReference())
        .iban(transaction.getIban())
        .dateTime(transaction.getDateTime())
        .amount(transaction.getAmount())
        .fee(transaction.getFee())
        .description(transaction.getDescription())
        .build();
  }
}
