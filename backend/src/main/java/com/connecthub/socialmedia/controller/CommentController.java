package com.connecthub.socialmedia.controller;

import com.connecthub.socialmedia.dto.ApiResponse;
import com.connecthub.socialmedia.dto.CommentDto;
import com.connecthub.socialmedia.entity.Comment;
import com.connecthub.socialmedia.security.UserPrincipal;
import com.connecthub.socialmedia.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

  @Autowired
  private CommentService commentService;

  @PostMapping("/post/{postId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> createComment(@PathVariable Long postId,
      @Valid @RequestBody CommentDto.CreateRequest createRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Comment comment = commentService.createComment(postId, currentUser.getId(), createRequest);
      CommentDto.Response commentResponse = new CommentDto.Response(comment);
      return ResponseEntity.ok(ApiResponse.success("댓글이 생성되었습니다.", commentResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @GetMapping("/post/{postId}")
  public ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
    try {
      List<CommentDto.Response> comments = commentService.getCommentsByPostId(postId);
      return ResponseEntity.ok(ApiResponse.success(comments));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @PutMapping("/{commentId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> updateComment(@PathVariable Long commentId,
      @Valid @RequestBody CommentDto.UpdateRequest updateRequest,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      Comment updatedComment = commentService.updateComment(commentId, currentUser.getId(), updateRequest);
      CommentDto.Response commentResponse = new CommentDto.Response(updatedComment);
      return ResponseEntity.ok(ApiResponse.success("댓글이 수정되었습니다.", commentResponse));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }

  @DeleteMapping("/{commentId}")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
      @AuthenticationPrincipal UserPrincipal currentUser) {
    try {
      commentService.deleteComment(commentId, currentUser.getId());
      return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다."));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.error(e.getMessage()));
    }
  }
}
