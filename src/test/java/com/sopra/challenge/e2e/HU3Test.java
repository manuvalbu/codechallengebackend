package com.sopra.challenge.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.business.domain.Channel;
import com.sopra.challenge.business.domain.Status;
import com.sopra.challenge.infrastructure.repository.dto.TransactionEntity;
import com.sopra.challenge.infrastructure.repository.persistence.AccountJpaRepository;
import com.sopra.challenge.infrastructure.repository.persistence.TransactionJpaRepository;
import java.util.HashMap;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HU3Test {

  @LocalServerPort
  int randomServerPort;
  RestTemplate restTemplate = new RestTemplate();

  String path = "/challenge/transaction/status";

  @Autowired
  TransactionJpaRepository transactionJpaRepository;

  @Autowired
  AccountJpaRepository accountJpaRepository;

  String reference;
  String iban;
  Double amount;
  Double fee;

  @BeforeEach
  void setUp() {
    //get reference that don't exist in database
    while (reference == null || transactionJpaRepository.findById(reference).isPresent()) {
      reference = UUID.randomUUID().toString();
    }
    //get iban that don't exist in database
    while (iban == null || accountJpaRepository.findById(iban).isPresent()) {
      iban = UUID.randomUUID().toString();
    }
    amount = 20.5;
    fee = 3.5;
    //create transactions in database
    TransactionEntity transaction = TransactionEntity
        .builder()
        .reference(reference)
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .build();
    transactionJpaRepository.save(transaction);
  }

  @AfterEach
  void cleanUp() {
    //delete transaction from database if persist
    if (transactionJpaRepository.findById(reference).isPresent()) {
      transactionJpaRepository.deleteById(reference);
    }
    //delete account from database if persist
    if (accountJpaRepository.findById(iban).isPresent()) {
      accountJpaRepository.deleteById(iban);
    }
  }

  @Test
  void HU3SearchTransactionOK_E2ET() throws JSONException {
    //Given
    final String uri = "http://localhost:" + randomServerPort + path;

    JSONObject transactionSearchJsonObject = new JSONObject();
    transactionSearchJsonObject.put("reference", reference);
    transactionSearchJsonObject.put("channel", Channel.CLIENT);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<String>(transactionSearchJsonObject.toString(),
        headers);

    //When
    ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(uri, request,
        HashMap.class);

    //Then
    assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    assertEquals(reference, responseEntity.getBody().get("reference"));
    assertEquals(Status.PENDING.name(), responseEntity.getBody().get("status"));
    assertEquals(amount - fee, responseEntity.getBody().get("amount"));
  }
}
