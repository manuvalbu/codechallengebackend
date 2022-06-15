package com.sopra.challenge.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.infrastructure.repository.persistence.AccountJpaRepository;
import com.sopra.challenge.infrastructure.repository.persistence.TransactionJpaRepository;
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
class HU1Test {

  @LocalServerPort
  int randomServerPort;
  RestTemplate restTemplate = new RestTemplate();

  @Autowired
  TransactionJpaRepository transactionJpaRepository;
  @Autowired
  AccountJpaRepository accountJpaRepository;

  String path = "/challenge/transaction";

  String reference;
  String iban;

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
  }

  @AfterEach
  void cleanUp() {
    //delete transaction from database if persists
    if (transactionJpaRepository.findById(reference).isPresent()) {
      transactionJpaRepository.deleteById(reference);
    }
    //delete account from database if persists
    if (accountJpaRepository.findById(iban).isPresent()) {
      accountJpaRepository.deleteById(iban);
    }
  }

  @Test
  void HU1TransactionCreatedOK_E2ET() throws JSONException {
    //Given
    final String uri = "http://localhost:" + randomServerPort + path;

    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("reference", reference);
    transactionJsonObject.put("iban", iban);
    transactionJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    transactionJsonObject.put("amount", 193.38);
    transactionJsonObject.put("fee", 3.18);
    transactionJsonObject.put("description", "Restaurant payment");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<String>(transactionJsonObject.toString(), headers);

    //When
    ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, request, Void.class);

    //Then
    assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
  }
}

