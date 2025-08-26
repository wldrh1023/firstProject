package com.connecthub.socialmedia.dto;

import com.connecthub.socialmedia.entity.Notification;

import java.time.LocalDateTime;

public class NotificationDto {

  // 알림 응답 DTO
  public static class Response {
    private Long id;
    private String type;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;

    // 생성자
    public Response() {
    }

    public Response(Notification notification) {
      this.id = notification.getId();
      this.type = notification.getType();
      this.message = notification.getMessage();
      this.isRead = notification.getIsRead();
      this.createdAt = notification.getCreatedAt();
    }

    // Getter와 Setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    public boolean isRead() {
      return isRead;
    }

    public void setRead(boolean read) {
      isRead = read;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }
  }
}
