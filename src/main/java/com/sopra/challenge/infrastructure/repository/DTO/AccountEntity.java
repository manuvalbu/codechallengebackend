package com.sopra.challenge.infrastructure.repository.DTO;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "ACCOUNT")
public class AccountEntity {

  @Id
  private String iban;
  private Double amount;

}
