package com.sopra.challenge.infrastructure.repository;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.output.ITransactionRepository;
import com.sopra.challenge.infrastructure.mapper.TransactionDomainInfrastructureMapper;
import com.sopra.challenge.infrastructure.repository.dto.TransactionEntity;
import com.sopra.challenge.infrastructure.repository.persistence.TransactionJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository implements ITransactionRepository {

  TransactionJpaRepository transactionJpaRepository;

  public TransactionRepository(TransactionJpaRepository transactionJpaRepository) {
    this.transactionJpaRepository = transactionJpaRepository;
  }

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

  @Override
  public List<Transaction> searchAll(Optional<String> iban, Optional<String> sortDir) {
    List<TransactionEntity> transactionEntities;

    Sort sort = sortDir.filter(s -> s.equalsIgnoreCase(Sort.Direction.DESC.name()))
        .map(s -> Sort.by("amount").descending())
        .orElse(Sort.by("amount").ascending());

    if (sortDir.isPresent()) {
      transactionEntities = iban.isPresent() ? transactionJpaRepository.findByIban(iban.get(), sort)
          : transactionJpaRepository.findAll(sort);
    } else {
      transactionEntities = iban.isPresent() ? transactionJpaRepository.findByIban(iban.get())
          : transactionJpaRepository.findAll();
    }

    return transactionEntities.stream()
        .map(TransactionDomainInfrastructureMapper::toDomainTransaction)
        .collect(Collectors.toList());
  }
}
