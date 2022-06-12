package com.sopra.challenge.infrastructure.repository.persistence;

import com.sopra.challenge.infrastructure.repository.dto.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<AccountEntity,String> {
}
