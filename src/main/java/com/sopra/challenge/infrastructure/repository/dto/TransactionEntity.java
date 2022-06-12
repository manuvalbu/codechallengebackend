package com.sopra.challenge.infrastructure.repository.dto;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "TRANSACTION")
public class TransactionEntity {

  @Id
  String reference;

  @NotNull
  String iban;

  LocalDateTime dateTime;

  @NotNull
  Double amount;

  Double fee;

  String description;

}
