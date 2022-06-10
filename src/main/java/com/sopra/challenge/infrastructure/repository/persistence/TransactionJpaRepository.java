package com.sopra.challenge.infrastructure.repository.persistence;

import com.sopra.challenge.infrastructure.repository.DTO.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, String> {
}
