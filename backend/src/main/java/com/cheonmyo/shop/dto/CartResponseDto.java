package com.cheonmyo.shop.dto;

import com.cheonmyo.shop.entity.Item;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CartResponseDto {
  private int itemId;
  private String itemName;
  private String imgPath;
  private int price;
  private int discountPer;
  private LocalDateTime addedAt;

  public CartResponseDto(Item item, LocalDateTime addedAt) {
    this.itemId = item.getId();
    this.itemName = item.getName();
    this.imgPath = item.getImgPath();
    this.price = item.getPrice();
    this.discountPer = item.getDiscountPer();
    this.addedAt = addedAt;
  }
}

