package com.cheonmyo.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private int memberId;

  @Column(length = 50, nullable = false)
  private String name;

  @Column(length = 500, nullable = false)
  private String address;

  @Column(length = 10, nullable = false)
  private String payment;

  @Column(length = 16)
  private String cardNumber;

  @Column(length = 500, nullable = false)
  private String items;

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", memberId=" + memberId +
        ", name='" + name + '\'' +
        ", address='" + address + '\'' +
        ", payment='" + payment + '\'' +
        ", cardNumber='" + cardNumber + '\'' +
        ", items='" + items + '\'' +
        '}';
  }
}