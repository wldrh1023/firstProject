package com.cheonmyo.shop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cheonmyo.shop.entity.Member;
import com.cheonmyo.shop.repository.MemberRepository;
import com.cheonmyo.shop.service.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*")
public class AccountController {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  JwtService jwtService;

  @PostMapping("/api/account/login")
  public ResponseEntity login(@RequestBody Map<String, String> params,
      HttpServletResponse res) {
    Member member = memberRepository.findByEmailAndPassword(params.get("email"), params.get("password"));

    if (member != null) {
      int id = member.getId();
      String token = jwtService.getToken("id", id);

      Cookie cookie = new Cookie("token", token);
      cookie.setHttpOnly(true);
      cookie.setPath("/");

      res.addCookie(cookie);

      return new ResponseEntity<>(id, HttpStatus.OK);
    }

    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
  public ResponseEntity check(@CookieValue(value = "token", required = false) String token) {
    Claims claims = jwtService.getClaims(token);

    if (claims != null) {
      int id = Integer.parseInt(claims.get("id").toString());
      return new ResponseEntity<>(id, HttpStatus.OK);
    }

    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
