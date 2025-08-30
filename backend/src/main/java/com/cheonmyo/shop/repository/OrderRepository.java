package com.cheonmyo.shop.repository;

import com.cheonmyo.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
  List<Order> findByMemberIdOrderByIdDesc(int memberId);

  @Query("SELECT o FROM Order o WHERE o.memberId = :memberId ORDER BY o.id DESC")
  List<Order> findOrdersWithTime(@Param("memberId") int memberId);
}
