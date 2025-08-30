package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.LoginRequestDto;
import com.cheonmyo.shop.dto.LoginResponseDto;
import com.cheonmyo.shop.dto.SignupRequestDto;
import com.cheonmyo.shop.dto.SignupResponseDto;

public interface AccountService {
  LoginResponseDto login(LoginRequestDto request);

  void logout();

  Integer checkAuth(String token);

  SignupResponseDto signup(SignupRequestDto request);
}
