package com.sopra.challenge.it;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.infrastructure.repository.AccountRepository;
import com.sopra.challenge.infrastructure.repository.TransactionRepository;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApiServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransactionRepository transactionRepositoryMock;
  @MockBean
  private AccountRepository accountRepositoryMock;

  @Test
  void createTransactionOk() throws Exception {
    //Given
    given(accountRepositoryMock.find(any(String.class))).willReturn(Optional.empty());

    JSONObject personJsonObject = new JSONObject();
    personJsonObject.put("reference", "12345A");
    personJsonObject.put("iban", "ES9820385778983000760236");
    personJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    personJsonObject.put("amount", 193.38);
    personJsonObject.put("fee", 3.18);
    personJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post("/transaction")
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON));
    //Then
    verify(accountRepositoryMock, times(1)).find(any(String.class));
    verify(transactionRepositoryMock, times(1)).save(any(Transaction.class));
  }

  @Test
  void createTransactionFailNegativeCredit() throws Exception {
    //Given
    given(accountRepositoryMock.find(any(String.class))).willReturn(Optional.empty());

    JSONObject personJsonObject = new JSONObject();
    personJsonObject.put("reference", "12345A");
    personJsonObject.put("iban", "ES9820385778983000760236");
    personJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    personJsonObject.put("amount", -193.38);
    personJsonObject.put("fee", 3.18);
    personJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post("/transaction")
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isFailedDependency())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsString("credit")));
    //Then
    verify(accountRepositoryMock, times(1)).find(any(String.class));
    verify(transactionRepositoryMock, times(0)).save(any(Transaction.class));
  }


  @Test
  void createTransactionParameterFailNoIban() throws Exception {
    //Given
    JSONObject personJsonObject = new JSONObject();
    personJsonObject.put("reference", "12345A");
    personJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    personJsonObject.put("amount", 100.0);
    personJsonObject.put("fee", 3.18);
    personJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post("/transaction")
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsString("IBAN")));
    //Then
    verify(accountRepositoryMock, times(0)).find(any(String.class));
    verify(transactionRepositoryMock, times(0)).save(any(Transaction.class));
  }

  @Test
  void createTransactionParameterFail0Amount() throws Exception {
    //Given
    JSONObject personJsonObject = new JSONObject();
    personJsonObject.put("reference", "12345A");
    personJsonObject.put("iban", "ES9820385778983000760236");
    personJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    personJsonObject.put("amount", 0);
    personJsonObject.put("fee", 3.18);
    personJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post("/transaction")
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsString("amount")));
    //Then
    verify(accountRepositoryMock, times(0)).find(any(String.class));
    verify(transactionRepositoryMock, times(0)).save(any(Transaction.class));
  }
}
