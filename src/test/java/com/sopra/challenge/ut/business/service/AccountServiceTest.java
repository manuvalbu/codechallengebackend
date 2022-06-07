package com.sopra.challenge.ut.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.business.service.AccountService;
import com.sopra.challenge.infrastructure.repository.AccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  AccountRepository accountRepository;

  @InjectMocks
  AccountService accountService;

  @Test
  void retrieveCreditOkTest() {
    //given
    String iban = "ES9820385778983000760236";
    Account account = Account
        .builder()
        .iban(iban)
        .amount(0.0)
        .build();
    given(accountRepository.find(iban)).willReturn(Optional.of(account));
    //when
    accountService.retrieveCredit(iban);
    //then
    verify(accountRepository, times(1)).find(iban);
  }

  @Test
  void retrieveCreditNoAccountTest() {
    //given
    String iban = "ES9820385778983000760236";
    given(accountRepository.find(iban)).willReturn(Optional.empty());
    //when
    Double amount = accountService.retrieveCredit(iban);
    //then
    verify(accountRepository, times(1)).find(iban);
    verify(accountRepository, times(1)).save(any(Account.class));
    assertEquals(0.0, amount);
  }
}
