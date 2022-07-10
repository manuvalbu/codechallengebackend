package com.sopra.challenge.it;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sopra.challenge.business.domain.Channel;
import com.sopra.challenge.business.domain.Status;
import com.sopra.challenge.business.domain.Transaction;
import com.sopra.challenge.business.exception.CreditException;
import com.sopra.challenge.business.port.input.ITransactionService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
class ApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ITransactionService transactionServiceMock;

  String pathCreate = "/challenge/transaction";

  String pathSearch = "/challenge/transactions";

  String pathInfo = "/challenge/transaction/status";

  String iban = "ES9820385778983000760236";
  String iban2 = "ES9820385778983000760237";

  String reference = "1234";

  Double amount = 150.5;
  Double fee = 1.5;


  String ibanParameter = "?iban=" + iban;

  String sortParameter = "?sort=" + Sort.Direction.DESC.name();

  String ibanAndSortParameters = "?iban=" + iban + "&sort=" + Sort.Direction.DESC.name();

  @Test
  void createTransactionOk_IT() throws Exception {
    //Given
    given(transactionServiceMock.createTransaction(any(Transaction.class))).willReturn(0.0);

    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("iban", iban);
    transactionJsonObject.put("amount", amount);

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
    ArgumentCaptor<Transaction> transactionArgument = ArgumentCaptor.forClass(Transaction.class);
    verify(transactionServiceMock, times(1)).createTransaction(transactionArgument.capture());
    assertEquals(iban, transactionArgument.getValue().getIban());
    assertEquals(amount, transactionArgument.getValue().getAmount());
  }

  @Test
  void createTransactionFailsNegativeCredit_IT() throws Exception {
    //Given
    given(transactionServiceMock.createTransaction(any(Transaction.class))).willThrow(
        CreditException.class);

    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("iban", iban);
    transactionJsonObject.put("amount", amount);

    //When
    this.mockMvc
        .perform(
            post(pathCreate)
                .contentType(APPLICATION_JSON)
                .content(transactionJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isFailedDependency());
  }


  @Test
  void createTransactionParameterFailsNoIban_IT() throws Exception {
    //Given
    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("amount", 100.0);

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
        .andExpect(content().string(Matchers.containsStringIgnoringCase("iban")));
  }

  @Test
  void createTransactionParameterFails0Amount_IT() throws Exception {
    //Given
    JSONObject transactionJsonObject = new JSONObject();
    transactionJsonObject.put("iban", iban);
    transactionJsonObject.put("amount", 0);

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
        .andExpect(content().string(Matchers.containsStringIgnoringCase("amount")));
  }

  @Test
  void searchAllTransactionsOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    ArrayList<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction1);
    transactions.add(transaction2);
    transactions.add(transaction3);

    given(transactionServiceMock.searchTransactions(Optional.empty(), Optional.empty())).willReturn(
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
  }

  @Test
  void searchTransactionsIbanOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .iban(iban2)
        .amount(amount)
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    ArrayList<Transaction> transactionsIban1 = new ArrayList<>();
    transactionsIban1.add(transaction1);
    transactionsIban1.add(transaction3);

    given(transactionServiceMock.searchTransactions(Optional.of(iban),
        Optional.empty())).willReturn(transactionsIban1);
    //When
    this.mockMvc.perform(MockMvcRequestBuilders.
            get(pathSearch + ibanParameter))
        .andDo(print())
        //Then
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[0].iban").value(iban))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[1].iban").value(iban));
  }

  @Test
  void searchAllTransactionsSortedDescOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .iban(iban2)
        .amount(amount)
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    ArrayList<Transaction> transactions = new ArrayList<>();
    transactions.add(transaction1);
    transactions.add(transaction2);
    transactions.add(transaction3);

    given(transactionServiceMock.searchTransactions(Optional.empty(),
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
  }

  @Test
  void searchAllTransactionsIbanSortedDescOk_IT() throws Exception {
    //Given
    Transaction transaction1 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    Transaction transaction2 = Transaction
        .builder()
        .iban(iban2)
        .amount(amount)
        .build();
    Transaction transaction3 = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .build();
    ArrayList<Transaction> transactionsIban1 = new ArrayList<>();
    transactionsIban1.add(transaction1);
    transactionsIban1.add(transaction3);

    given(transactionServiceMock.searchTransactions(Optional.of(iban),
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
            MockMvcResultMatchers.jsonPath("$.[0].iban").value(iban))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.[1].iban").value(iban));
  }

  @Test
  void searchTransactionOk_IT() throws Exception {
    //Given
    Transaction transaction = Transaction
        .builder()
        .iban(iban)
        .amount(amount)
        .fee(fee)
        .build();
    given(transactionServiceMock.searchTransaction(any(String.class))).willReturn(
        Optional.ofNullable(transaction));

    JSONObject transactionSearchJsonObject = new JSONObject();
    transactionSearchJsonObject.put("reference", reference);
    transactionSearchJsonObject.put("channel", Channel.CLIENT);

    //When
    this.mockMvc
        .perform(
            post(pathInfo)
                .contentType(APPLICATION_JSON)
                .content(transactionSearchJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.status").value(Status.PENDING.name()));
    //Then
    verify(transactionServiceMock, times(1)).searchTransaction(reference);
  }

  @Test
  void searchTransactionNoReferenceFail_IT() throws Exception {
    //Given
    JSONObject transactionSearchJsonObject = new JSONObject();
    transactionSearchJsonObject.put("channel", Channel.CLIENT);

    //When
    this.mockMvc
        .perform(
            post(pathInfo)
                .contentType(APPLICATION_JSON)
                .content(transactionSearchJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsStringIgnoringCase("reference")));
    //Then
    verify(transactionServiceMock, times(0)).searchTransaction(any());
  }

  @Test
  void searchTransactionNoChannelFail_IT() throws Exception {
    //Given
    JSONObject transactionSearchJsonObject = new JSONObject();
    transactionSearchJsonObject.put("reference", reference);

    //When
    this.mockMvc
        .perform(
            post(pathInfo)
                .contentType(APPLICATION_JSON)
                .content(transactionSearchJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsStringIgnoringCase("channel")));
    //Then
    verify(transactionServiceMock, times(0)).searchTransaction(any());
  }

  @Test
  void searchTransactionIncorrectChannelFail_IT() throws Exception {
    //Given
    JSONObject transactionSearchJsonObject = new JSONObject();
    transactionSearchJsonObject.put("reference", reference);
    transactionSearchJsonObject.put("channel", "channel");

    //When
    this.mockMvc
        .perform(
            post(pathInfo)
                .contentType(APPLICATION_JSON)
                .content(transactionSearchJsonObject.toString()))
        .andDo(print())
        //Then
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(Matchers.containsStringIgnoringCase("channel")));
    //Then
    verify(transactionServiceMock, times(0)).searchTransaction(any());
  }
}
