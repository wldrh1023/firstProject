package com.cheonmyo.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cheonmyo.shop.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
  Member findByEmailAndPassword(String email, String password);
  
}
