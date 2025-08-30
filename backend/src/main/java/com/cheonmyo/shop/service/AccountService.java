package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.LoginRequestDto;
import com.cheonmyo.shop.dto.LoginResponseDto;

public interface AccountService {
  LoginResponseDto login(LoginRequestDto request);

  void logout();

  Integer checkAuth(String token);
}
