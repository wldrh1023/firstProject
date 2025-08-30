package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {
  List<ItemResponseDto> getAllItems();
}

