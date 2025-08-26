package com.connecthub.socialmedia.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, length = 50)
  @NotBlank(message = "알림 타입은 필수입니다")
  private String type;

  @Column(nullable = false, length = 500)
  @NotBlank(message = "알림 메시지는 필수입니다")
  private String message;

  @Column(name = "is_read", nullable = false)
  private Boolean isRead = false;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  // 알림 타입 상수
  public static final String TYPE_LIKE = "LIKE";
  public static final String TYPE_COMMENT = "COMMENT";
  public static final String TYPE_FOLLOW = "FOLLOW";

  // 기본 생성자
  public Notification() {
  }

  // 생성자
  public Notification(User user, String type, String message) {
    this.user = user;
    this.type = type;
    this.message = message;
    this.isRead = false;
  }

  // Getter와 Setter
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
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

  public Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(Boolean isRead) {
    this.isRead = isRead;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  // 편의 메서드
  public void markAsRead() {
    this.isRead = true;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Notification notification = (Notification) obj;
    return id != null && id.equals(notification.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    return "Notification{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", message='" + message + '\'' +
        ", isRead=" + isRead +
        ", createdAt=" + createdAt +
        '}';
  }
}
