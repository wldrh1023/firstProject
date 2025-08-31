package com.cheonmyo.shop.dto;

import com.cheonmyo.shop.entity.Item;
import lombok.Getter;

@Getter
public class ItemResponseDto {
  private int id;
  private String name;
  private String imgPath;
  private int price;
  private int discountPer;

  public ItemResponseDto(Item item) {
    this.id = item.getId();
    this.name = item.getName();
    this.imgPath = item.getImgPath();
    this.price = item.getPrice();
    this.discountPer = item.getDiscountPer();
  }
}
