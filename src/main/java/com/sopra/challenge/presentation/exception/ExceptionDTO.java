package com.sopra.challenge.presentation.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExceptionDTO {

  private String code;
  private String message;

}
