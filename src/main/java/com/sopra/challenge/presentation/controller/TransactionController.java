package com.sopra.challenge.presentation.controller;

import com.sopra.challenge.business.domain.Channel;
import com.sopra.challenge.business.domain.Status;
import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.TransactionParameterException;
import com.sopra.challenge.business.port.input.ITransactionService;
import com.sopra.challenge.presentation.dto.TransactionDTO;
import com.sopra.challenge.presentation.dto.TransactionStatusDTO;
import com.sopra.challenge.presentation.mapper.TransactionDomainPresentationMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      @RequestParam("iban") Optional<String> iban,
      @RequestParam("sort") Optional<String> sortDir) {
    log.info("search Transactions");
    List<Transaction> transactions = transactionService.searchTransactions(iban, sortDir);
    List<TransactionDTO> transactionDTOs = transactions
        .stream()
        .map(TransactionDomainPresentationMapper::toPresentationTransaction)
        .collect(Collectors.toList());
    log.info("successfully searched transactions");
    return ResponseEntity.ok(transactionDTOs);
  }

  @PostMapping("/transaction/status")
  public ResponseEntity<TransactionStatusDTO> searchTransaction(
      @RequestBody TransactionStatusDTO transactionStatusDTOInput) {

    if (transactionStatusDTOInput.getReference() == null || transactionStatusDTOInput.getReference()
        .isBlank()) {
      throw new TransactionParameterException("Missing Reference");
    }
    if (transactionStatusDTOInput.getChannel() == null || transactionStatusDTOInput.getChannel()
        .isBlank()) {
      throw new TransactionParameterException("Missing Channel");
    }
    try {
      Channel.valueOf(transactionStatusDTOInput.getChannel());
    } catch (Exception e) {
      throw new TransactionParameterException(e.getMessage());
    }

    log.info(
        "searching for Transaction status, Request body : " + transactionStatusDTOInput.toString());
    Optional<Transaction> transactionOptional = transactionService.searchTransaction(
        transactionStatusDTOInput.getReference());

    if (transactionOptional.isEmpty()) {
      log.info("Transaction not found, reference : " + transactionStatusDTOInput.getReference());
      TransactionStatusDTO transactionStatusDTOOutput = TransactionStatusDTO
          .builder()
          .reference(transactionStatusDTOInput.getReference())
          .status(Status.INVALID)
          .build();
      return ResponseEntity.ok(transactionStatusDTOOutput);
    }

    Transaction transaction = transactionOptional.get();
    log.info(
        "successfully searched Transaction, reference : " + transaction.getReference());
    TransactionStatusDTO transactionStatusDTOOutput = TransactionStatusDTO
        .builder()
        .reference(transaction.getReference())
        .amount(transaction.getAmount())
        .fee(transaction.getFee())
        .status(Status.PENDING)
        .build();
    return ResponseEntity.ok(transactionStatusDTOOutput);
  }
}

