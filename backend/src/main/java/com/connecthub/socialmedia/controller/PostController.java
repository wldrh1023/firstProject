package com.connecthub.socialmedia.controller;

import com.connecthub.socialmedia.dto.ApiResponse;
import com.connecthub.socialmedia.dto.PostDto;
import com.connecthub.socialmedia.entity.Post;
import com.connecthub.socialmedia.security.UserPrincipal;
import com.connecthub.socialmedia.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  @Autowired
  private PostService postService;

  @PostMapping
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> createPost(@Valid @RequestBody PostDto.CreateRequest createRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Post post = postService.createPost(currentUser.getId(), createRequest);
      PostDto.Response postResponse = postService.getPostResponse(post.getId(), currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success("게시물이 생성되었습니다.", postResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/{postId}")
  public ResponseEntity<?> getPost(@PathVariable Long postId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Long currentUserId = currentUser != null ? currentUser.getId() : null;
      PostDto.Response postResponse = postService.getPostResponse(postId, currentUserId);
      return ResponseEntity.ok(ApiResponse.success(postResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/feed")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> getFeed(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      Page<PostDto.SimpleResponse> posts = postService.getFeed(currentUser.getId(), pageable);
      return ResponseEntity.ok(ApiResponse.success(posts));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<?> getUserPosts(@PathVariable Long userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      Long currentUserId = currentUser != null ? currentUser.getId() : null;
      Page<PostDto.SimpleResponse> posts = postService.getUserPosts(userId, currentUserId, pageable);
      return ResponseEntity.ok(ApiResponse.success(posts));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/search")
  public ResponseEntity<?> searchPosts(@RequestParam String keyword,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Pageable pageable = PageRequest.of(page, size);
      Long currentUserId = currentUser != null ? currentUser.getId() : null;
      Page<PostDto.SimpleResponse> posts = postService.searchPosts(keyword, currentUserId, pageable);
      return ResponseEntity.ok(ApiResponse.success(posts));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{postId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> updatePost(@PathVariable Long postId,
      @Valid @RequestBody PostDto.UpdateRequest updateRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Post updatedPost = postService.updatePost(postId, currentUser.getId(), updateRequest);
      PostDto.Response postResponse = postService.getPostResponse(updatedPost.getId(), currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success("게시물이 수정되었습니다.", postResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{postId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> deletePost(@PathVariable Long postId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      postService.deletePost(postId, currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success("게시물이 삭제되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
