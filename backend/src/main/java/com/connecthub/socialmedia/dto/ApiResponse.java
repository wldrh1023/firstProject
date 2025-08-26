package com.connecthub.socialmedia.dto;

public class ApiResponse<T> {
  private boolean success;
  private String message;
  private T data;

  // 생성자
  public ApiResponse() {
  }

  public ApiResponse(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public ApiResponse(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }

  // 정적 팩토리 메서드들
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(true, "성공", data);
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>(true, message, data);
  }

  public static <T> ApiResponse<T> success(String message) {
    return new ApiResponse<>(true, message);
  }

  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>(false, message);
  }

  public static <T> ApiResponse<T> error(String message, T data) {
    return new ApiResponse<>(false, message, data);
  }

  // Getter와 Setter
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
