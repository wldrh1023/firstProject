package com.cheonmyo.shop.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException() {
    super("요청한 리소스를 찾을 수 없습니다.");
  }
}

