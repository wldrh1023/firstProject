package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.LoginRequestDto;
import com.cheonmyo.shop.dto.LoginResponseDto;
import com.cheonmyo.shop.dto.SignupRequestDto;
import com.cheonmyo.shop.dto.SignupResponseDto;
import com.cheonmyo.shop.entity.Member;
import com.cheonmyo.shop.exception.NotFoundException;
import com.cheonmyo.shop.exception.UnauthorizedException;
import com.cheonmyo.shop.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cheonmyo.shop.util.BCryptUtil;

@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private JwtService jwtService;

  @Override
  public LoginResponseDto login(LoginRequestDto request) {
    Member member = memberRepository.findByEmail(request.getEmail());

    if (member == null) {
      throw new NotFoundException("회원 정보가 존재하지 않습니다.");
    }

    // BCrypt로 암호화된 비밀번호 검증
    if (!BCryptUtil.check(request.getPassword(), member.getPassword())) {
      throw new NotFoundException("비밀번호가 일치하지 않습니다.");
    }

    return new LoginResponseDto(member.getId(), "로그인에 성공했습니다.");
  }

  @Override
  public void logout() {
    // 로그아웃 로직은 주로 클라이언트에서 처리되거나 쿠키 삭제로 처리됨
    // 필요한 경우 추가 로직 구현
  }

  @Override
  public Integer checkAuth(String token) {
    Claims claims = jwtService.getClaims(token);

    if (claims != null) {
      return Integer.parseInt(claims.get("id").toString());
    }

    return null;
  }

  @Override
  public SignupResponseDto signup(SignupRequestDto request) {
    // 이메일 중복 확인
    if (memberRepository.findByEmail(request.getEmail()) != null) {
      return new SignupResponseDto("이미 존재하는 이메일입니다.", false);
    }

    // 새 회원 생성
    Member newMember = new Member();
    newMember.setEmail(request.getEmail());
    // 비밀번호 BCrypt 암호화
    String encodedPassword = BCryptUtil.hash(request.getPassword());
    newMember.setPassword(encodedPassword);

    memberRepository.save(newMember);

    return new SignupResponseDto("회원가입이 완료되었습니다.", true);
  }

  @Override
  public void withdraw(String token) {
    if (token == null || token.isEmpty()) {
      throw new UnauthorizedException("로그인이 필요합니다.");
    }

    Claims claims = jwtService.getClaims(token);
    if (claims == null) {
      throw new UnauthorizedException("유효하지 않은 토큰입니다.");
    }

    Integer memberId = Integer.parseInt(claims.get("id").toString());
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

    // 회원 삭제
    memberRepository.delete(member);
  }
}
