package com.sopra.challenge.infrastructure.repository.persistence;

import com.sopra.challenge.infrastructure.repository.DTO.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity,String> {
}
