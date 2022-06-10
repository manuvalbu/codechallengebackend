package com.sopra.challenge.infrastructure.repository;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.output.ITransactionRepository;
import com.sopra.challenge.infrastructure.mapper.TransactionDomainInfrastructureMapper;
import com.sopra.challenge.infrastructure.repository.DTO.TransactionEntity;
import com.sopra.challenge.infrastructure.repository.persistence.TransactionJpaRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository implements ITransactionRepository {

  @Autowired
  TransactionJpaRepository transactionJpaRepository;

  @Override
  public void create(Transaction transaction) {
    TransactionEntity transactionEntity = TransactionDomainInfrastructureMapper.toInfrastructureTransaction(
        transaction);
    transactionJpaRepository.save(transactionEntity);
  }

  @Override
  public Optional<Transaction> search(String reference) {
    Optional<TransactionEntity> optionalTransactionEntity = transactionJpaRepository.findById(
        reference);
    return optionalTransactionEntity.map(
        TransactionDomainInfrastructureMapper::toDomainTransaction);
  }
}
