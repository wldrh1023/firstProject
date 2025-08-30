package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.OrderRequestDto;
import com.cheonmyo.shop.dto.OrderResponseDto;
import com.cheonmyo.shop.entity.Order;
import com.cheonmyo.shop.exception.NotFoundException;
import com.cheonmyo.shop.exception.UnauthorizedException;
import com.cheonmyo.shop.repository.CartRepository;
import com.cheonmyo.shop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CartRepository cartRepository;

  @Override
  public List<OrderResponseDto> getOrders(int memberId) {
    List<Order> orders = orderRepository.findByMemberIdOrderByIdDesc(memberId);
    return orders.stream()
        .map(OrderResponseDto::new)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void createOrder(int memberId, OrderRequestDto request) {
    Order newOrder = new Order();
    newOrder.setMemberId(memberId);
    newOrder.setName(request.getName());
    newOrder.setAddress(request.getAddress());
    newOrder.setPayment(request.getPayment());
    newOrder.setCardNumber(request.getCardNumber());
    newOrder.setItems(request.getItems());

    Order savedOrder = orderRepository.save(newOrder);
    cartRepository.deleteByMemberId(memberId);

    System.out.println("주문 생성 완료 - ID: " + savedOrder.getId() + ", 생성시간: " + savedOrder.getCreatedAt());
  }

  @Override
  @Transactional
  public void deleteOrder(int memberId, Integer orderId) {
    System.out.println("삭제 요청 - memberId: " + memberId + ", orderId: " + orderId);

    try {
      Order order = orderRepository.findById(orderId)
          .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다. (ID: " + orderId + ")"));

      System.out.println("주문 찾음 - orderId: " + order.getId() + ", memberId: " + order.getMemberId());

      // 본인의 주문만 삭제 가능
      if (order.getMemberId() != memberId) {
        throw new UnauthorizedException(
            "본인의 주문만 삭제할 수 있습니다. (요청자: " + memberId + ", 주문자: " + order.getMemberId() + ")");
      }

      orderRepository.delete(order);
      System.out.println("주문 삭제 완료 - orderId: " + orderId);
    } catch (Exception e) {
      System.err.println("주문 삭제 중 오류 발생: " + e.getMessage());
      throw e;
    }
  }
}
