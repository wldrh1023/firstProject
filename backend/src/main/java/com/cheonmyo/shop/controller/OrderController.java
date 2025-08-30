package com.cheonmyo.shop.controller;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.cheonmyo.shop.entity.Order;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class OrderController {

  @Autowired
  private OrderService orderService;

  @Autowired
  private JwtService jwtService;

  @GetMapping("/api/orders")
  public ResponseEntity<List<Map<String, Object>>> getOrders(
      @CookieValue(value = "token", required = false) String token) {
    if (token == null || token.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try {
      Integer memberId = jwtService.getId(token);
      List<OrderResponseDto> orderDtos = orderService.getOrders(memberId);

      // Map으로 변환
      List<Map<String, Object>> result = new ArrayList<>();
      for (OrderResponseDto orderDto : orderDtos) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", orderDto.getId());
        orderMap.put("name", orderDto.getName());
        orderMap.put("address", orderDto.getAddress());
        orderMap.put("payment", orderDto.getPayment());
        orderMap.put("cardNumber", orderDto.getCardNumber());
        orderMap.put("items", orderDto.getItems());

        result.add(orderMap);
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      System.out.println("주문 조회 중 오류: " + e.getMessage());
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
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