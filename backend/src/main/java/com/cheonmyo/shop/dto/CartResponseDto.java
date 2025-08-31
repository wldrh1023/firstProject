package com.cheonmyo.shop.dto;

import com.cheonmyo.shop.entity.Item;
import lombok.Getter;

@Getter
public class CartResponseDto {
  private int itemId;
  private String itemName;
  private String imgPath;
  private int price;
  private int discountPer;

  public CartResponseDto(Item item) {
    this.itemId = item.getId();
    this.itemName = item.getName();
    this.imgPath = item.getImgPath();
    this.price = item.getPrice();
    this.discountPer = item.getDiscountPer();
  }
}
