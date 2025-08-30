package com.cheonmyo.shop.config;

import com.cheonmyo.shop.entity.Member;
import com.cheonmyo.shop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("h2")
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private MemberRepository memberRepository;

  @Override
  public void run(String... args) throws Exception {
    // 테스트 회원 추가
    if (memberRepository.count() == 0) {
      Member testMember = new Member("test@example.com", "password123");
      memberRepository.save(testMember);
      System.out.println("테스트 회원이 생성되었습니다: " + testMember.getEmail());
    }

    System.out.println("테스트 데이터 초기화가 완료되었습니다.");
  }
}
