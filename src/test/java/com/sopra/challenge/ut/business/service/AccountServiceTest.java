package com.sopra.challenge.ut.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sopra.challenge.business.domain.Account;
import com.sopra.challenge.business.port.output.IAccountRepository;
import com.sopra.challenge.business.service.AccountService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  IAccountRepository accountRepository;

  @InjectMocks
  AccountService accountService;

  @Test
  void retrieveCreditOk_UT() {
    //Given
    String iban = "ES9820385778983000760236";
    Account account = Account
        .builder()
        .iban(iban)
        .amount(0.0)
        .build();
    given(accountRepository.search(iban)).willReturn(Optional.of(account));
    //when
    accountService.retrieveCredit(iban);
    //Then
    verify(accountRepository, times(1)).search(iban);
  }

  @Test
  void retrieveCreditNoAccount_UT() {
    //Given
    String iban = "ES9820385778983000760236";
    given(accountRepository.search(iban)).willReturn(Optional.empty());
    //When
    Double amount = accountService.retrieveCredit(iban);
    //Then
    verify(accountRepository, times(1)).search(iban);
    verify(accountRepository, times(1)).create(any(Account.class));
    assertEquals(0.0, amount);
  }
}
