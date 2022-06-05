package com.sopra.challenge.business.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Transaction {

  String reference;
  String iban;
  LocalDateTime dateTime;
  Double amount;
  Double fee;
  String description;

}

