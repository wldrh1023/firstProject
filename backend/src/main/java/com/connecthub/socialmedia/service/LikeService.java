package com.connecthub.socialmedia.service;

import com.connecthub.socialmedia.entity.Like;
import com.connecthub.socialmedia.entity.Post;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {

  @Autowired
  private LikeRepository likeRepository;

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private NotificationService notificationService;

  public void toggleLike(Long postId, Long userId) {
    Post post = postService.getPostById(postId);
    User user = userService.getUserById(userId);

    if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
      // 좋아요 취소
      likeRepository.deleteByPostIdAndUserId(postId, userId);
    } else {
      // 좋아요 추가
      Like like = new Like(post, user);
      likeRepository.save(like);

      // 자신의 게시물이 아닌 경우에만 알림 생성
      if (!post.getUser().getId().equals(userId)) {
        String message = user.getUsername() + "님이 회원님의 게시물을 좋아합니다.";
        notificationService.createNotification(
            post.getUser().getId(),
            "LIKE",
            message);
      }
    }
  }

  @Transactional(readOnly = true)
  public boolean isLikedByUser(Long postId, Long userId) {
    return likeRepository.existsByPostIdAndUserId(postId, userId);
  }

  @Transactional(readOnly = true)
  public long getLikeCount(Long postId) {
    return likeRepository.countByPostId(postId);
  }
}
