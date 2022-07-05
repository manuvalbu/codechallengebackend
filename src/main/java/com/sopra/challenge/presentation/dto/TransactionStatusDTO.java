package com.sopra.challenge.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sopra.challenge.business.domain.Channel;
import com.sopra.challenge.business.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@JsonInclude(Include.NON_NULL)
public class TransactionStatusDTO {

  String reference;
  Channel channel;
  Status status;
  Double amount;
  Double fee;

}
