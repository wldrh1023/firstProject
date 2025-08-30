package com.cheonmyo.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
  private int memberId;
  private String message;
}

