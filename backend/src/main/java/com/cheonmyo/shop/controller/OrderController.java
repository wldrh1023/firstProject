package com.cheonmyo.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cheonmyo.shop.dto.OrderRequestDto;
import com.cheonmyo.shop.dto.OrderResponseDto;
import com.cheonmyo.shop.exception.UnauthorizedException;
import com.cheonmyo.shop.service.JwtService;
import com.cheonmyo.shop.service.OrderService;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private JwtService jwtService;

  @GetMapping("/api/orders")
  public ResponseEntity<List<OrderResponseDto>> getOrder(
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new UnauthorizedException();
    }

    int memberId = jwtService.getId(token);
    List<OrderResponseDto> orders = orderService.getOrders(memberId);
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  @PostMapping("/api/orders")
  public ResponseEntity<Void> pushOrder(
      @RequestBody OrderRequestDto dto,
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new UnauthorizedException();
    }

    int memberId = jwtService.getId(token);
    orderService.createOrder(memberId, dto);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/api/orders/{orderId}")
  public ResponseEntity<Void> deleteOrder(
      @PathVariable Integer orderId,
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new UnauthorizedException();
    }

    int memberId = jwtService.getId(token);
    orderService.deleteOrder(memberId, orderId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}