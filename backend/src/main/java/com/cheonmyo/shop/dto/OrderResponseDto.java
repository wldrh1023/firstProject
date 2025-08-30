package com.cheonmyo.shop.dto;

import com.cheonmyo.shop.entity.Order;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResponseDto {
  @JsonProperty("id")
  private Integer id = 999;

  @JsonProperty("name")
  private String name = "테스트이름";

  @JsonProperty("address")
  private String address = "테스트주소";

  @JsonProperty("payment")
  private String payment = "테스트결제";

  @JsonProperty("cardNumber")
  private String cardNumber = "테스트카드";

  @JsonProperty("items")
  private String items = "테스트상품";

  @JsonProperty("date")
  private String date = "2024-08-30";

  public OrderResponseDto(Order order) {
    // 실제 데이터로 덮어쓰기
    this.id = order.getId();
    this.name = order.getName();
    this.address = order.getAddress();
    this.payment = order.getPayment();
    this.cardNumber = order.getCardNumber();
    this.items = order.getItems();

    // 날짜는 고정값 유지
    this.date = "2024-08-30";
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

  public String getDate() {
    return date;
  }
}
