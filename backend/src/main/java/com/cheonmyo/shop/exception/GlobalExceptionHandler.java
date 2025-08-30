package com.cheonmyo.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        "UNAUTHORIZED",
        e.getMessage(),
        HttpStatus.UNAUTHORIZED.value());
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        "NOT_FOUND",
        e.getMessage(),
        HttpStatus.NOT_FOUND.value());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getStatusCode().toString(),
        e.getReason() != null ? e.getReason() : "오류가 발생했습니다.",
        e.getStatusCode().value());
    return new ResponseEntity<>(errorResponse, e.getStatusCode());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(
        "INTERNAL_SERVER_ERROR",
        "서버 내부 오류가 발생했습니다.",
        HttpStatus.INTERNAL_SERVER_ERROR.value());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

