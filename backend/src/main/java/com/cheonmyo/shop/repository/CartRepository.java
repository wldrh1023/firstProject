package com.cheonmyo.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheonmyo.shop.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{

  List<Cart> findByMemberId(int memberId);

  Cart findByMemberIdAndItemId(int memberId, int itemId);

  void deleteByMemberId(int memberId);
}
