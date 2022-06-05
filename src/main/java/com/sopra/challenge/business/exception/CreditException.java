package com.sopra.challenge.business.exception;

import lombok.Getter;

@Getter
public class CreditException extends Exception {

  private final Double amount;

  private final Double fee;

  public CreditException(Double amount, Double fee) {
    super("No credit available for the operation with amount " + amount + " and fee " + fee);
    this.amount = amount;
    this.fee = fee;
  }
}
