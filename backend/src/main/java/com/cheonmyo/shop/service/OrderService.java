package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.OrderRequestDto;
import com.cheonmyo.shop.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
  List<OrderResponseDto> getOrders(int memberId);

  void createOrder(int memberId, OrderRequestDto request);

  void deleteOrder(int memberId, Integer orderId);
}
