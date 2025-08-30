package com.cheonmyo.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cheonmyo.shop.dto.ItemResponseDto;
import com.cheonmyo.shop.service.ItemService;

@RestController
public class ItemController {

  @Autowired
  private ItemService itemService;

  @GetMapping("/api/items")
  public List<ItemResponseDto> getItems() {
    return itemService.getAllItems();
  }
}
