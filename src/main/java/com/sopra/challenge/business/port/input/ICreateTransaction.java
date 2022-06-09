package com.sopra.challenge.business.port.input;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;

public interface ICreateTransaction {

  Double createTransaction(Transaction transaction) throws CreditException;

}