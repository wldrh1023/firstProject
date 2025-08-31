package com.cheonmyo.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cheonmyo.shop.dto.LoginRequestDto;
import com.cheonmyo.shop.dto.LoginResponseDto;
import com.cheonmyo.shop.dto.SignupRequestDto;
import com.cheonmyo.shop.dto.SignupResponseDto;
import com.cheonmyo.shop.service.AccountService;
import com.cheonmyo.shop.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
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

  @DeleteMapping("/api/account/withdraw")
  public ResponseEntity<String> withdraw(@CookieValue(value = "token", required = false) String token,
      HttpServletResponse res) {
    try {
      accountService.withdraw(token);

      // 쿠키 삭제
      Cookie cookie = new Cookie("token", null);
      cookie.setPath("/");
      cookie.setMaxAge(0);
      res.addCookie(cookie);

      return new ResponseEntity<>("회원탈퇴가 완료되었습니다.", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/api/health")
  public ResponseEntity<String> health() {
    System.out.println("=== 헬스체크 API 호출됨 ===");
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

  @PostMapping("/api/account/signup")
  public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto request) {
    System.out.println("=== 회원가입 API 호출됨 ===");
    System.out.println("요청 받음: " + request);
    System.out.println("이메일: " + request.getEmail());
    System.out.println("비밀번호: " + request.getPassword());

    try {
      SignupResponseDto response = accountService.signup(request);
      System.out.println("회원가입 성공: " + response.getMessage());
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      System.out.println("회원가입 실패: " + e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }
}
