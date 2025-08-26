package com.connecthub.socialmedia.repository;

import com.connecthub.socialmedia.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Page<Notification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

  List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

  @Query("SELECT COUNT(n) FROM Notification n WHERE n.user.id = :userId AND n.isRead = false")
  long countUnreadByUserId(@Param("userId") Long userId);

  @Modifying
  @Query("UPDATE Notification n SET n.isRead = true WHERE n.user.id = :userId AND n.isRead = false")
  void markAllAsReadByUserId(@Param("userId") Long userId);

  @Modifying
  @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :notificationId AND n.user.id = :userId")
  void markAsReadByIdAndUserId(@Param("notificationId") Long notificationId, @Param("userId") Long userId);

  void deleteByUserId(Long userId);
}
