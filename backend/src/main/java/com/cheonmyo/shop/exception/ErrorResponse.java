package com.cheonmyo.shop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private final String error;
  private final String message;
  private final int status;
}

