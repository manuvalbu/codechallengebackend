package com.sopra.challenge.presentation.controller;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.port.input.ITransactionService;
import com.sopra.challenge.presentation.dto.TransactionDTO;
import com.sopra.challenge.presentation.mapper.TransactionDomainPresentationMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  public ResponseEntity<List<TransactionDTO>> searchTransactions(
      @PathParam("iban") Optional<String> iban,
      @PathParam("sort") Optional<String> sortDir) {
    log.info("search Transactions");
    List<Transaction> transactions = transactionService.searchTransactions(iban, sortDir);
    List<TransactionDTO> transactionDTOs = transactions
        .stream()
        .map(TransactionDomainPresentationMapper::toPresentationTransaction)
        .collect(Collectors.toList());
    log.info("successfully searched transactions");
    return ResponseEntity.ok(transactionDTOs);
  }
}

