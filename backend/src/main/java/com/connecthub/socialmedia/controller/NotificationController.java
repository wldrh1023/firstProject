package com.connecthub.socialmedia.controller;

import com.connecthub.socialmedia.dto.ApiResponse;
import com.connecthub.socialmedia.dto.NotificationDto;
import com.connecthub.socialmedia.security.UserPrincipal;
import com.connecthub.socialmedia.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasRole('USER')")
public class NotificationController {

  @Autowired
  private NotificationService notificationService;

  @GetMapping
  public ResponseEntity<?> getNotifications(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      Page<NotificationDto.Response> notifications = notificationService.getUserNotifications(currentUser.getId(),
          pageable);
      return ResponseEntity.ok(ApiResponse.success(notifications));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/unread")
  public ResponseEntity<?> getUnreadNotifications(@AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      List<NotificationDto.Response> notifications = notificationService.getUnreadNotifications(currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success(notifications));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/unread/count")
  public ResponseEntity<?> getUnreadCount(@AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      long unreadCount = notificationService.getUnreadCount(currentUser.getId());

      Map<String, Long> response = new HashMap<>();
      response.put("unreadCount", unreadCount);

      return ResponseEntity.ok(ApiResponse.success(response));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{notificationId}/read")
  public ResponseEntity<?> markAsRead(@PathVariable Long notificationId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      notificationService.markAsRead(notificationId, currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success("알림을 읽음으로 표시했습니다."));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/read-all")
  public ResponseEntity<?> markAllAsRead(@AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      notificationService.markAllAsRead(currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success("모든 알림을 읽음으로 표시했습니다."));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<?> deleteNotification(@PathVariable Long notificationId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      notificationService.deleteNotification(notificationId, currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success("알림이 삭제되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
