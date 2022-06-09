package com.sopra.challenge.presentation.controller;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.input.ICreateTransaction;
import com.sopra.challenge.presentation.DTO.TransactionDTO;
import com.sopra.challenge.presentation.mapper.TransactionDomainPresentationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

  @Autowired
  private ICreateTransaction createTransactionService;

  public TransactionController(ICreateTransaction createTransactionService) {
    this.createTransactionService = createTransactionService;
  }

  @PostMapping("/transaction")
  public ResponseEntity<Object> createTransaction(@RequestBody TransactionDTO transactionDTO) {
    Transaction transaction = TransactionDomainPresentationMapper.toDomainTransaction(
        transactionDTO);
    return ResponseEntity.ok(createTransactionService.createTransaction(transaction));
  }
}

