package com.cheonmyo.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

  private String name;
  private String address;
  private String payment;
  private String cardNumber;
  private String items;
  private String time;

  @Override
  public String toString() {
    return "OrderRequestDto{" +
        "name='" + name + '\'' +
        ", address='" + address + '\'' +
        ", payment='" + payment + '\'' +
        ", cardNumber='" + cardNumber + '\'' +
        ", items='" + items + '\'' +
        ", time='" + time + '\'' +
        '}';
  }
}
