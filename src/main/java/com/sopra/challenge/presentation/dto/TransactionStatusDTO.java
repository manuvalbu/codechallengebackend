package com.sopra.challenge.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sopra.challenge.business.domain.Channel;
import com.sopra.challenge.business.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class TransactionStatusDTO {

  String reference;
  String channel;
  Status status;
  Double amount;
  Double fee;

}
