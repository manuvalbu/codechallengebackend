package com.sopra.challenge.presentation.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TransactionDTO {

  String reference;
  String iban;
  LocalDateTime dateTime;
  Double amount;
  Double fee;
  String description;

}
