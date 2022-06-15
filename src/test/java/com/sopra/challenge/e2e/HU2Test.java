package com.sopra.challenge.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sopra.challenge.infrastructure.repository.dto.TransactionEntity;
import com.sopra.challenge.infrastructure.repository.persistence.AccountJpaRepository;
import com.sopra.challenge.infrastructure.repository.persistence.TransactionJpaRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HU2Test {

  @LocalServerPort
  int randomServerPort;
  RestTemplate restTemplate = new RestTemplate();

  String path = "/challenge/transactions";

  @Autowired
  TransactionJpaRepository transactionJpaRepository;

  @Autowired
  AccountJpaRepository accountJpaRepository;

  String reference;
  String iban1;
  String iban2;

  @BeforeEach
  void setUp() {
    reference = UUID.randomUUID().toString();
    iban1 = UUID.randomUUID().toString();
    iban2 = UUID.randomUUID().toString();
    //create transactions in database
    for (int i = 1; i <= 5; i++) {
      TransactionEntity transaction = TransactionEntity
          .builder()
          .reference(reference + i)
          .iban(Math.floorMod(i, 2) == 0 ? iban1 : iban2)
          .amount((double) (i * 10))
          .fee((double) i)
          .build();
      transactionJpaRepository.save(transaction);
    }
  }

  @AfterEach
  void cleanUp() {
    //delete transactions from database if persist
    for (int i = 1; i <= 5; i++) {
      if (transactionJpaRepository.findById(reference + i).isPresent()) {
        transactionJpaRepository.deleteById(reference + i);
      }
    }
    //delete accounts from database if persist
    if (accountJpaRepository.findById(iban1).isPresent()) {
      accountJpaRepository.deleteById(iban1);
    }
    if (accountJpaRepository.findById(iban2).isPresent()) {
      accountJpaRepository.deleteById(iban2);
    }
  }

  @Test
  void HU2FindAllTransactions_E2ET() {
    //Given
    final String uri = "http://localhost:" + randomServerPort + path;
    //When
    ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);
    //Then
    assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    assertEquals(5, Objects.requireNonNull(responseEntity.getBody()).size());
  }

  @Test
  void HU2FindAllTransactionsIbanSortedDesc_E2ET() {
    //Given
    String ibanAndSortParameters = "?iban=" + iban1 + "&sort=" + Sort.Direction.DESC.name();
    final String uri = "http://localhost:" + randomServerPort + path + ibanAndSortParameters;
    //When
    ResponseEntity<List> responseEntity = restTemplate.getForEntity(uri, List.class);
    //Then
    assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    List<LinkedHashMap> transactions = responseEntity.getBody();
    assertEquals(2, transactions.size());
    assertEquals(iban1, transactions.get(0).get("iban"));
    assertEquals(iban1, transactions.get(1).get("iban"));
    assertTrue(Double.parseDouble(transactions.get(0).get("amount").toString())
        >= Double.parseDouble(transactions.get(1).get("amount").toString()));
  }
}
