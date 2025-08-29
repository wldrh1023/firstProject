package com.cheonmyo.shop.controller;

import java.util.List;

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

import com.cheonmyo.shop.dto.OrderDto;
import com.cheonmyo.shop.entity.Order;
import com.cheonmyo.shop.repository.CartRepository;
import com.cheonmyo.shop.repository.OrderRepository;
import com.cheonmyo.shop.service.JwtService;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  CartRepository cartRepository;

  @Autowired
  JwtService jwtService;

  @GetMapping("/api/orders")
  public ResponseEntity getOrder(
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    int memberId = jwtService.getId(token);
    List<Order> orders = orderRepository.findByMemberIdOrderByIdDesc(memberId);
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  @Transactional
  @PostMapping("/api/orders")
  public ResponseEntity pushOrder(
      @RequestBody OrderDto dto,
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    int memberId = jwtService.getId(token);
    Order newOrder = new Order();

    newOrder.setMemberId(memberId);
    newOrder.setName(dto.getName());
    newOrder.setAddress(dto.getAddress());
    newOrder.setPayment(dto.getPayment());
    newOrder.setCardNumber(dto.getCardNumber());
    newOrder.setItems(dto.getItems());

    orderRepository.save(newOrder);
    cartRepository.deleteByMemberId(memberId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}