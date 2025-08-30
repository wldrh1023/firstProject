package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.CartResponseDto;

import java.util.List;

public interface CartService {
  List<CartResponseDto> getCartItems(int memberId);

  void addCartItem(int memberId, int itemId);

  void removeCartItem(int memberId, int itemId);
}

