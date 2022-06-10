package com.sopra.challenge.infrastructure.mapper;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.infrastructure.repository.DTO.TransactionEntity;

public class TransactionDomainInfrastructureMapper {

  private TransactionDomainInfrastructureMapper() {
  }

  public static Transaction toDomainTransaction(TransactionEntity transactionEntity) {
    return Transaction
        .builder()
        .reference(transactionEntity.getReference())
        .iban(transactionEntity.getIban())
        .dateTime(transactionEntity.getDateTime())
        .amount(transactionEntity.getAmount())
        .fee(transactionEntity.getFee())
        .description(transactionEntity.getDescription())
        .build();
  }

  public static TransactionEntity toInfrastructureTransaction(Transaction transaction) {
    return TransactionEntity
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
