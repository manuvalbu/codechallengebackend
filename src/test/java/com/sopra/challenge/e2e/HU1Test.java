package com.sopra.challenge.e2e;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
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

  String path = "/challenge/transaction";

  @Test
  void HU1TransactionCreatedOK_E2ET() throws JSONException {
    //Given
    final String uri = "http://localhost:" + randomServerPort + path;

    JSONObject personJsonObject = new JSONObject();
    personJsonObject.put("reference", "12345A");
    personJsonObject.put("iban", "ES9820385778983000760236");
    personJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    personJsonObject.put("amount", 193.38);
    personJsonObject.put("fee", 3.18);
    personJsonObject.put("description", "Restaurant payment");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> request = new HttpEntity<String>(personJsonObject.toString(), headers);

    //When
    ResponseEntity<Void> responseEntity = restTemplate.postForEntity(uri, request, Void.class);

    //Then
    assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

  }
}

