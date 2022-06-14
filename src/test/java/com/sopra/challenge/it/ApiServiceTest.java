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
import com.sopra.challenge.business.port.output.IAccountRepository;
import com.sopra.challenge.business.port.output.ITransactionRepository;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApiServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ITransactionRepository transactionRepositoryMock;
  @MockBean
  private IAccountRepository accountRepositoryMock;

  String pathCreate = "/challenge/transaction";

  String pathSearch = "/challenge/transactions";

  @Test
  void createTransactionOk_IT() throws Exception {
    //Given
    given(accountRepositoryMock.search(any(String.class))).willReturn(Optional.empty());

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
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isOk());
    //Then
    verify(accountRepositoryMock, times(1)).search(any(String.class));
    verify(transactionRepositoryMock, times(1)).create(any(Transaction.class));
  }

  @Test
  void createTransactionFailsNegativeCredit_IT() throws Exception {
    //Given
    given(accountRepositoryMock.search(any(String.class))).willReturn(Optional.empty());

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
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isFailedDependency())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsString("credit")));
    //Then
    verify(accountRepositoryMock, times(1)).search(any(String.class));
    verify(transactionRepositoryMock, times(0)).create(any(Transaction.class));
  }


  @Test
  void createTransactionParameterFailsNoIban_IT() throws Exception {
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
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsString("IBAN")));
    //Then
    verify(accountRepositoryMock, times(0)).search(any(String.class));
    verify(transactionRepositoryMock, times(0)).create(any(Transaction.class));
  }

  @Test
  void createTransactionParameterFails0Amount_IT() throws Exception {
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
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(personJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsString("amount")));
    //Then
    verify(accountRepositoryMock, times(0)).search(any(String.class));
    verify(transactionRepositoryMock, times(0)).create(any(Transaction.class));
  }

  @Test
  void findTransactionsOk_IT() throws Exception {
    //When
    this.mockMvc.perform(MockMvcRequestBuilders.
            get(pathSearch))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
