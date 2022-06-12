package com.sopra.challenge.presentation.exception;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.exception.TransactionParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class ExceptionController {

  @ExceptionHandler({TransactionParameterException.class})
  public ResponseEntity<ExceptionDTO> handleTransactionParameterException(
      final TransactionParameterException e) {
    ExceptionDTO exceptionDTO = ExceptionDTO
        .builder()
        .code(TransactionParameterException.class.getName())
        .message(e.getMessage())
        .build();
    log.info(exceptionDTO.toString());
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(exceptionDTO);
  }

  @ExceptionHandler({CreditException.class})
  public ResponseEntity<ExceptionDTO> handleCreditException(
      final CreditException e) {
    ExceptionDTO exceptionDTO = ExceptionDTO
        .builder()
        .code(CreditException.class.getName())
        .message(e.getMessage())
        .build();
    log.info(exceptionDTO.toString());
    return ResponseEntity
        .status(HttpStatus.FAILED_DEPENDENCY)
        .body(exceptionDTO);
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ExceptionDTO> handleUnknownException(
      final Exception e) {
    ExceptionDTO exceptionDTO = ExceptionDTO
        .builder()
        .code(Exception.class.getName())
        .message(e.getMessage())
        .build();
    log.info(exceptionDTO.toString());
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(exceptionDTO);
  }
}
