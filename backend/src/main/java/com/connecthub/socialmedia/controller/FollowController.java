package com.connecthub.socialmedia.controller;

import com.connecthub.socialmedia.dto.ApiResponse;
import com.connecthub.socialmedia.security.UserPrincipal;
import com.connecthub.socialmedia.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

  @Autowired
  private FollowService followService;

  @PostMapping("/user/{userId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> toggleFollow(@PathVariable Long userId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      followService.toggleFollow(currentUser.getId(), userId);

      // 현재 팔로우 상태와 팔로워 수 반환
      boolean isFollowing = followService.isFollowing(currentUser.getId(), userId);
      long followerCount = followService.getFollowerCount(userId);

      Map<String, Object> response = new HashMap<>();
      response.put("isFollowing", isFollowing);
      response.put("followerCount", followerCount);

      String message = isFollowing ? "팔로우를 시작했습니다." : "팔로우를 취소했습니다.";
      return ResponseEntity.ok(ApiResponse.success(message, response));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getFollowStatus(@PathVariable Long userId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      long followerCount = followService.getFollowerCount(userId);
      long followingCount = followService.getFollowingCount(userId);
      boolean isFollowing = false;

      if (currentUser != null && !currentUser.getId().equals(userId)) {
        isFollowing = followService.isFollowing(currentUser.getId(), userId);
      }

      Map<String, Object> response = new HashMap<>();
      response.put("isFollowing", isFollowing);
      response.put("followerCount", followerCount);
      response.put("followingCount", followingCount);

      return ResponseEntity.ok(ApiResponse.success(response));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
