package com.sopra.challenge.infrastructure.mapper;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.infrastructure.repository.DTO.AccountEntity;

public class AccountDomainInfrastructureMapper {

  public static Account toDomainAccount(AccountEntity accountEntity) {
    return Account
        .builder()
        .iban(accountEntity.getIban())
        .amount(accountEntity.getAmount())
        .build();
  }

  public static AccountEntity toInfrastructureAccount(Account account) {
    return AccountEntity
        .builder()
        .iban(account.getIban())
        .amount(account.getAmount())
        .build();
  }
}
