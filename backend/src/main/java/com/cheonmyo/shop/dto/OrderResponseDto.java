package com.cheonmyo.shop.dto;

import com.cheonmyo.shop.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponseDto {
  private Integer id;
  private String name;
  private String address;
  private String payment;
  private String cardNumber;
  private String items;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public OrderResponseDto(Order order) {
    this.id = order.getId();
    this.name = order.getName();
    this.address = order.getAddress();
    this.payment = order.getPayment();
    this.cardNumber = order.getCardNumber();
    this.items = order.getItems();
    this.createdAt = order.getCreatedAt();
    this.updatedAt = order.getUpdatedAt();
  }
}
