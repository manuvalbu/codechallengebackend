package com.sopra.challenge.business.domain;

import com.sopra.challenge.business.exception.TransactionParameterException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Transaction {

  String reference;
  String iban;
  LocalDateTime dateTime;
  Double amount;
  Double fee;
  String description;

  Transaction(String reference, String iban, LocalDateTime dateTime, Double amount, Double fee,
      String description) {
    if (reference == null || reference.isBlank()) {
      reference = UUID.randomUUID().toString();
    }
    this.reference = reference;
    if (iban == null || iban.isBlank()) {
      throw new TransactionParameterException("no valid IBAN");
    }
    this.iban = iban;
    if (dateTime == null) {
      dateTime = LocalDateTime.now();
    }
    this.dateTime = dateTime;
    if (amount == null || amount == 0) {
      throw new TransactionParameterException("no valid amount");
    }
    this.amount = amount;
    if (fee == null) {
      fee = 0.0;
    }
    this.fee = fee;
    this.description = description;
  }
}

