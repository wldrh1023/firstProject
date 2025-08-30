package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.ItemResponseDto;
import com.cheonmyo.shop.entity.Item;
import com.cheonmyo.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

  @Autowired
  private ItemRepository itemRepository;

  @Override
  public List<ItemResponseDto> getAllItems() {
    List<Item> items = itemRepository.findAll();
    return items.stream()
        .map(ItemResponseDto::new)
        .collect(Collectors.toList());
  }
}

