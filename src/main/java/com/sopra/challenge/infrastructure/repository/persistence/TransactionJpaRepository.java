package com.sopra.challenge.infrastructure.repository.persistence;

import com.sopra.challenge.infrastructure.repository.dto.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
}
