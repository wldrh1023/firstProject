package com.cheonmyo.shop.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
  private String message;
  private boolean success;

  public SignupResponseDto(String message, boolean success) {
    this.message = message;
    this.success = success;
  }

}
