package com.connecthub.socialmedia.service;

import com.connecthub.socialmedia.dto.NotificationDto;
import com.connecthub.socialmedia.entity.Notification;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  @Autowired
  private UserService userService;

  public Notification createNotification(Long userId, String type, String message) {
    User user = userService.getUserById(userId);

    Notification notification = new Notification();
    notification.setUser(user);
    notification.setType(type);
    notification.setMessage(message);
    notification.setIsRead(false);

    return notificationRepository.save(notification);
  }

  @Transactional(readOnly = true)
  public Page<NotificationDto.Response> getUserNotifications(Long userId, Pageable pageable) {
    Page<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

    return notifications.map(NotificationDto.Response::new);
  }

  @Transactional(readOnly = true)
  public List<NotificationDto.Response> getUnreadNotifications(Long userId) {
    List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);

    return notifications.stream()
        .map(NotificationDto.Response::new)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public long getUnreadCount(Long userId) {
    return notificationRepository.countUnreadByUserId(userId);
  }

  public void markAsRead(Long notificationId, Long userId) {
    notificationRepository.markAsReadByIdAndUserId(notificationId, userId);
  }

  public void markAllAsRead(Long userId) {
    notificationRepository.markAllAsReadByUserId(userId);
  }

  public void deleteNotification(Long notificationId, Long userId) {
    Notification notification = notificationRepository.findById(notificationId)
        .orElseThrow(() -> new RuntimeException("알림을 찾을 수 없습니다."));

    // 소유자 확인
    if (!notification.getUser().getId().equals(userId)) {
      throw new RuntimeException("알림을 삭제할 권한이 없습니다.");
    }

    notificationRepository.delete(notification);
  }
}
