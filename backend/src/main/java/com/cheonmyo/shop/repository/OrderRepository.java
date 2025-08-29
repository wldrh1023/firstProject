package com.cheonmyo.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheonmyo.shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

  List<Order> findByMemberIdOrderByIdDesc(int memberId);
}
