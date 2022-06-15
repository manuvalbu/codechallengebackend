package com.sopra.challenge.infrastructure.repository.persistence;

import com.sopra.challenge.infrastructure.repository.dto.TransactionEntity;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {

  List<TransactionEntity> findByIban(String iban);

  List<TransactionEntity> findByIban(String iban, Sort sort);
}
