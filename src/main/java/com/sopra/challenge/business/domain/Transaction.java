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

  public static class TransactionBuilder {

    public TransactionBuilder reference(String reference) {
      if (reference == null || reference.isBlank()) {
        reference = UUID.randomUUID().toString();
      }
      this.reference = reference;
      return this;
    }

    public TransactionBuilder iban(String iban) throws TransactionParameterException {
      if (iban == null || iban.isBlank()) {
        throw new TransactionParameterException("no valid IBAN");
      }
      this.iban = iban;
      return this;
    }

    public TransactionBuilder dateTime(LocalDateTime dateTime) {
      if (dateTime == null) {
        dateTime = LocalDateTime.now();
      }
      this.dateTime = dateTime;
      return this;
    }

    public TransactionBuilder amount(Double amount) throws TransactionParameterException {
      if (amount == null || amount == 0) {
        throw new TransactionParameterException("no valid amount");
      }
      this.amount = amount;
      return this;
    }

    public TransactionBuilder fee(Double fee) {
      if (fee == null) {
        fee = 0.0;
      }
      this.fee = fee;
      return this;
    }
  }

}

