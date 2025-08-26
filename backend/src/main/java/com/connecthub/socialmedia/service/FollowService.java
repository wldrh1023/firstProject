package com.connecthub.socialmedia.service;

import com.connecthub.socialmedia.entity.Follow;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FollowService {

  @Autowired
  private FollowRepository followRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private NotificationService notificationService;

  public void toggleFollow(Long followerId, Long followingId) {
    if (followerId.equals(followingId)) {
      throw new RuntimeException("자기 자신을 팔로우할 수 없습니다.");
    }

    User follower = userService.getUserById(followerId);
    User following = userService.getUserById(followingId);

    if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
      // 언팔로우
      followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    } else {
      // 팔로우
      Follow follow = new Follow(follower, following);
      followRepository.save(follow);

      // 팔로우 알림 생성
      String message = follower.getUsername() + "님이 회원님을 팔로우하기 시작했습니다.";
      notificationService.createNotification(
          followingId,
          "FOLLOW",
          message);
    }
  }

  @Transactional(readOnly = true)
  public boolean isFollowing(Long followerId, Long followingId) {
    return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
  }

  @Transactional(readOnly = true)
  public long getFollowerCount(Long userId) {
    return followRepository.countFollowers(userId);
  }

  @Transactional(readOnly = true)
  public long getFollowingCount(Long userId) {
    return followRepository.countFollowing(userId);
  }
}
