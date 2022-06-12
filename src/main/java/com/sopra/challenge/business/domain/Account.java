package com.sopra.challenge.business.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Account {

  private String iban;
  private Double amount;

}
