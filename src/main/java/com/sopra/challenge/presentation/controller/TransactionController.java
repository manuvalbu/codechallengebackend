package com.sopra.challenge.presentation.controller;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.input.ITransactionService;
import com.sopra.challenge.infrastructure.repository.dto.TransactionEntity;
import com.sopra.challenge.presentation.dto.TransactionDTO;
import com.sopra.challenge.presentation.mapper.TransactionDomainPresentationMapper;
import java.util.ArrayList;
import java.util.List;
import javax.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/challenge")
@RestController
public class TransactionController {

  private final ITransactionService transactionService;

  public TransactionController(ITransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping("/transaction")
  public ResponseEntity<Void> createTransaction(@RequestBody TransactionDTO transactionDTO) {
    log.info("creating Transaction, Request body : " + transactionDTO.toString());
    Transaction transaction = TransactionDomainPresentationMapper.toDomainTransaction(
        transactionDTO);
    transactionService.createTransaction(transaction);
    log.info("successfully created Transaction, reference : " + transactionDTO.getReference());
    return ResponseEntity.ok(null);
  }

  @GetMapping("/transactions")
  public ResponseEntity<List<TransactionEntity>> searchTransactions(@PathParam("iban") String iban,
      @PathParam("sort") Integer sort) {
    log.info("search Transactions");
    //transactionService.search();
    log.info("successfully searched transactions");
    return ResponseEntity.ok(new ArrayList());
  }
}

