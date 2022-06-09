package com.sopra.challenge.presentation.DTO;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionDTO {

  String reference;
  String iban;
  LocalDateTime dateTime;
  Double amount;
  Double fee;
  String description;

}
