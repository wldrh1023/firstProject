package com.connecthub.socialmedia.controller;

import com.connecthub.socialmedia.dto.ApiResponse;
import com.connecthub.socialmedia.security.UserPrincipal;
import com.connecthub.socialmedia.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

  @Autowired
  private LikeService likeService;

  @PostMapping("/post/{postId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> toggleLike(@PathVariable Long postId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      likeService.toggleLike(postId, currentUser.getId());

      // 현재 좋아요 상태와 개수 반환
      boolean isLiked = likeService.isLikedByUser(postId, currentUser.getId());
      long likeCount = likeService.getLikeCount(postId);

      Map<String, Object> response = new HashMap<>();
      response.put("isLiked", isLiked);
      response.put("likeCount", likeCount);

      String message = isLiked ? "좋아요를 눌렀습니다." : "좋아요를 취소했습니다.";
      return ResponseEntity.ok(ApiResponse.success(message, response));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/post/{postId}")
  public ResponseEntity<?> getLikeStatus(@PathVariable Long postId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      long likeCount = likeService.getLikeCount(postId);
      boolean isLiked = false;

      if (currentUser != null) {
        isLiked = likeService.isLikedByUser(postId, currentUser.getId());
      }

      Map<String, Object> response = new HashMap<>();
      response.put("isLiked", isLiked);
      response.put("likeCount", likeCount);

      return ResponseEntity.ok(ApiResponse.success(response));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
