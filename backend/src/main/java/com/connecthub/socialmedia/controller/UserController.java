package com.connecthub.socialmedia.controller;

import com.connecthub.socialmedia.dto.ApiResponse;
import com.connecthub.socialmedia.dto.UserDto;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.security.UserPrincipal;
import com.connecthub.socialmedia.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      UserDto.Response userResponse = userService.getUserProfile(currentUser.getId(), currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success(userResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/{userId}")
  public ResponseEntity<?> getUserProfile(@PathVariable Long userId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Long currentUserId = currentUser != null ? currentUser.getId() : null;
      UserDto.Response userResponse = userService.getUserProfile(userId, currentUserId);
      return ResponseEntity.ok(ApiResponse.success(userResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<?> getUserByUsername(@PathVariable String username,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      User user = userService.getUserByUsername(username);
      Long currentUserId = currentUser != null ? currentUser.getId() : null;
      UserDto.Response userResponse = userService.getUserProfile(user.getId(), currentUserId);
      return ResponseEntity.ok(ApiResponse.success(userResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/me")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> updateProfile(@Valid @RequestBody UserDto.ProfileUpdateRequest updateRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      User updatedUser = userService.updateProfile(currentUser.getId(), updateRequest);
      UserDto.Response userResponse = new UserDto.Response(updatedUser);
      return ResponseEntity.ok(ApiResponse.success("프로필이 업데이트되었습니다.", userResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/search")
  public ResponseEntity<?> searchUsers(@RequestParam String keyword,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Long currentUserId = currentUser != null ? currentUser.getId() : null;
      List<UserDto.Response> users = userService.searchUsers(keyword, currentUserId);
      return ResponseEntity.ok(ApiResponse.success(users));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/{userId}/followers")
  public ResponseEntity<?> getFollowers(@PathVariable Long userId) {
    try {
      List<UserDto.SimpleResponse> followers = userService.getFollowers(userId);
      return ResponseEntity.ok(ApiResponse.success(followers));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/{userId}/following")
  public ResponseEntity<?> getFollowing(@PathVariable Long userId) {
    try {
      List<UserDto.SimpleResponse> following = userService.getFollowing(userId);
      return ResponseEntity.ok(ApiResponse.success(following));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
