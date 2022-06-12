package com.sopra.challenge.business.domain;

import com.sopra.challenge.business.exception.TransactionParameterException;
import java.time.LocalDateTime;
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

    public TransactionBuilder iban(String iban) throws TransactionParameterException {
      if (iban == null || iban.isBlank()) {
        throw new TransactionParameterException("no valid IBAN");
      }
      this.iban = iban;
      return this;
    }

    public TransactionBuilder amount(Double amount) throws TransactionParameterException {
      if (amount == null || amount == 0) {
        throw new TransactionParameterException("no valid amount");
      }
      this.amount = amount;
      return this;
    }

  }

}

