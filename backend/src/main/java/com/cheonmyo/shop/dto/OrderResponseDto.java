package com.cheonmyo.shop.dto;

import com.cheonmyo.shop.entity.Order;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResponseDto {
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("address")
  private String address;

  @JsonProperty("payment")
  private String payment;

  @JsonProperty("cardNumber")
  private String cardNumber;

  @JsonProperty("items")
  private String items;

  public OrderResponseDto(Order order) {
    // 실제 데이터로 덮어쓰기
    this.id = order.getId();
    this.name = order.getName();
    this.address = order.getAddress();
    this.payment = order.getPayment();
    this.cardNumber = order.getCardNumber();
    this.items = order.getItems();

    // 날짜는 고정값 유지
  }

  // Getter 메서드들
  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getPayment() {
    return payment;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public String getItems() {
    return items;
  }

}
