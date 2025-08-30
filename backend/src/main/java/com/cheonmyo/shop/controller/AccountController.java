package com.cheonmyo.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cheonmyo.shop.dto.LoginRequestDto;
import com.cheonmyo.shop.dto.LoginResponseDto;
import com.cheonmyo.shop.service.AccountService;
import com.cheonmyo.shop.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*")
public class AccountController {

  @Autowired
  private AccountService accountService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/api/account/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request,
      HttpServletResponse res) {
    LoginResponseDto response = accountService.login(request);

    String token = jwtService.getToken("id", response.getMemberId());

    Cookie cookie = new Cookie("token", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");

    res.addCookie(cookie);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/api/account/logout")
  public ResponseEntity logout(HttpServletResponse res) {
    Cookie cookie = new Cookie("token", null);
    cookie.setPath("/");
    cookie.setMaxAge(0);

    res.addCookie(cookie);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/api/account/check")
  public ResponseEntity<Integer> check(@CookieValue(value = "token", required = false) String token) {
    Integer memberId = accountService.checkAuth(token);
    return new ResponseEntity<>(memberId, HttpStatus.OK);
  }
}
