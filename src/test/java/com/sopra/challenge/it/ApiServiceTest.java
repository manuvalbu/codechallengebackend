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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


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

  String iban1 = "ES9820385778983000760236";
  String iban2 = "ES9820385778983000760237";

  String ibanParameter = "?iban=" + iban1;

  String sortParameter = "?sort=" + Sort.Direction.DESC.name();

  String ibanAndSortParameters = "?iban=" + iban1 + "&sort=" + Sort.Direction.DESC.name();

  @Test
  void createTransactionOk_IT() throws Exception {
    //Given
    given(accountRepositoryMock.search(any(String.class))).willReturn(Optional.empty());

    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("reference", "12345A");
    transactionJsonObject.put("iban", iban1);
    transactionJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    transactionJsonObject.put("amount", 193.38);
    transactionJsonObject.put("fee", 3.18);
    transactionJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(transactionJsonObject.toString()))
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

    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("reference", "12345A");
    transactionJsonObject.put("iban", iban1);
    transactionJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    transactionJsonObject.put("amount", -193.38);
    transactionJsonObject.put("fee", 3.18);
    transactionJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(transactionJsonObject.toString()))
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
    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("reference", "12345A");
    transactionJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    transactionJsonObject.put("amount", 100.0);
    transactionJsonObject.put("fee", 3.18);
    transactionJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(transactionJsonObject.toString()))
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
    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("reference", "12345A");
    transactionJsonObject.put("iban", iban1);
    transactionJsonObject.put("date", "2019-07-16T16:55:42.000Z");
    transactionJsonObject.put("amount", 0);
    transactionJsonObject.put("fee", 3.18);
    transactionJsonObject.put("description", "Restaurant payment");

    //When
    this.mockMvc
        .perform(
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(transactionJsonObject.toString()))
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
  void searchAllTransactionsOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .reference("12345A1")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(100.0)
        .fee(3.18)
        .description("Restaurant payment 1")
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .reference("12345A2")
        .iban(iban2)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(105.0)
        .fee(3.13)
        .description("Restaurant payment 2")
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .reference("12345A3")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(100.0)
        .fee(3.18)
        .description("Restaurant payment 3")
        .build();
    ArrayList<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction1);
    transactions.add(transaction2);
    transactions.add(transaction3);

    given(transactionRepositoryMock.searchAll(Optional.empty(), Optional.empty())).willReturn(
        transactions);
    //When
    this.mockMvc.perform(MockMvcRequestBuilders.
            get(pathSearch))
        .andDo(print())
        //Then
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[0].reference").value(transaction1.getReference()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[1].reference").value(transaction2.getReference()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[2].reference").value(transaction3.getReference()));
    //Then
    verify(transactionRepositoryMock, times(1)).searchAll(Optional.empty(), Optional.empty());
  }

  @Test
  void searchTransactionsIbanOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .reference("12345A1")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(100.0)
        .fee(3.18)
        .description("Restaurant payment 1")
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .reference("12345A2")
        .iban(iban2)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(105.0)
        .fee(3.13)
        .description("Restaurant payment 2")
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .reference("12345A1")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(100.0)
        .fee(3.18)
        .description("Restaurant payment 3")
        .build();
    ArrayList<Transaction> transactionsIban1 = new ArrayList<>();
    transactionsIban1.add(transaction1);
    transactionsIban1.add(transaction3);

    given(transactionRepositoryMock.searchAll(Optional.of(iban1),
        Optional.empty())).willReturn(transactionsIban1);
    //When
    this.mockMvc.perform(MockMvcRequestBuilders.
            get(pathSearch + ibanParameter))
        .andDo(print())
        //Then
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[0].iban").value(iban1))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[1].iban").value(iban1));
    //Then
    verify(transactionRepositoryMock, times(1)).searchAll(Optional.of(iban1), Optional.empty());
  }

  @Test
  void searchAllTransactionsSortedDescOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .reference("12345A1")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(150.0)
        .fee(3.18)
        .description("Restaurant payment 1")
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .reference("12345A2")
        .iban(iban2)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(105.0)
        .fee(3.13)
        .description("Restaurant payment 2")
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .reference("12345A3")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(100.0)
        .fee(3.18)
        .description("Restaurant payment 3")
        .build();
    ArrayList<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction1);
    transactions.add(transaction2);
    transactions.add(transaction3);

    given(transactionRepositoryMock.searchAll(Optional.empty(),
        Optional.of(Sort.Direction.DESC.name()))).willReturn(transactions);
    //When
    this.mockMvc.perform(MockMvcRequestBuilders.
            get(pathSearch + sortParameter))
        .andDo(print())
        //Then
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[0].amount")
                .value(
                    transactions
                        .stream()
                        .max(Comparator.comparing(Transaction::getAmount))
                        .get()
                        .getAmount()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[2].amount")
                .value(
                    transactions
                        .stream()
                        .min(Comparator.comparing(Transaction::getAmount))
                        .get()
                        .getAmount()));
    //Then
    verify(transactionRepositoryMock, times(1)).searchAll(Optional.empty(),
        Optional.of(Sort.Direction.DESC.name()));
  }

  @Test
  void searchAllTransactionsIbanSortedDescOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .reference("12345A1")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(150.0)
        .fee(3.18)
        .description("Restaurant payment 1")
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .reference("12345A2")
        .iban(iban2)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(105.0)
        .fee(3.13)
        .description("Restaurant payment 2")
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .reference("12345A3")
        .iban(iban1)
        .dateTime(LocalDateTime.now().minusDays(1))
        .amount(100.0)
        .fee(3.18)
        .description("Restaurant payment 3")
        .build();
    ArrayList<Transaction> transactionsIban1 = new ArrayList<>();
    transactionsIban1.add(transaction1);
    transactionsIban1.add(transaction3);

    given(transactionRepositoryMock.searchAll(Optional.of(iban1),
        Optional.of(Sort.Direction.DESC.name()))).willReturn(transactionsIban1);
    //When
    this.mockMvc.perform(MockMvcRequestBuilders.
            get(pathSearch + ibanAndSortParameters))
        .andDo(print())
        //Then
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[0].amount")
                .value(
                    transactionsIban1
                        .stream()
                        .max(Comparator.comparing(Transaction::getAmount))
                        .get()
                        .getAmount()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[1].amount")
                .value(
                    transactionsIban1
                        .stream()
                        .min(Comparator.comparing(Transaction::getAmount))
                        .get()
                        .getAmount()))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[0].iban").value(iban1))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[1].iban").value(iban1));
    //Then
    verify(transactionRepositoryMock, times(1)).searchAll(Optional.of(iban1),
        Optional.of(Sort.Direction.DESC.name()));
  }
}
