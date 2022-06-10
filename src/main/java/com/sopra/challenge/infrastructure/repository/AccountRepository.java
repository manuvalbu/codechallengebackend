package com.sopra.challenge.infrastructure.repository;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.business.port.output.IAccountRepository;
import com.sopra.challenge.infrastructure.mapper.AccountDomainInfrastructureMapper;
import com.sopra.challenge.infrastructure.repository.DTO.AccountEntity;
import com.sopra.challenge.infrastructure.repository.persistence.AccountJpaRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository implements IAccountRepository {

  @Autowired
  AccountJpaRepository accountJpaRepository;

  public Optional<Account> find(String iban) {
    Optional<AccountEntity> optionalAccountEntity = accountJpaRepository.findById(iban);
    return optionalAccountEntity.map(AccountDomainInfrastructureMapper::toDomainAccount);
  }

  @Override
  public void save(Account account) {
    AccountEntity accountEntity = AccountDomainInfrastructureMapper.toInfrastructureAccount(
        account);
    accountJpaRepository.save(accountEntity);
  }
}
