package com.sopra.challenge.infrastructure.repository;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.business.port.output.IAccountRepository;
import com.sopra.challenge.infrastructure.mapper.AccountDomainInfrastructureMapper;
import com.sopra.challenge.infrastructure.repository.dto.AccountEntity;
import com.sopra.challenge.infrastructure.repository.persistence.AccountJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository implements IAccountRepository {

  AccountJpaRepository accountJpaRepository;

  public AccountRepository(AccountJpaRepository accountJpaRepository) {
    this.accountJpaRepository = accountJpaRepository;
  }

  public Optional<Account> search(String iban) {
    Optional<AccountEntity> optionalAccountEntity = accountJpaRepository.findById(iban);
    return optionalAccountEntity.map(AccountDomainInfrastructureMapper::toDomainAccount);
  }

  @Override
  public void create(Account account) {
    AccountEntity accountEntity = AccountDomainInfrastructureMapper.toInfrastructureAccount(
        account);
    accountJpaRepository.save(accountEntity);
  }
}
