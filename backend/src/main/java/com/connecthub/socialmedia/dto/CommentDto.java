package com.connecthub.socialmedia.dto;

import com.connecthub.socialmedia.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CommentDto {

  // 댓글 생성 요청 DTO
  public static class CreateRequest {
    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(max = 500, message = "댓글 내용은 500자를 초과할 수 없습니다")
    private String content;

    // Getter와 Setter
    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }

  // 댓글 수정 요청 DTO
  public static class UpdateRequest {
    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(max = 500, message = "댓글 내용은 500자를 초과할 수 없습니다")
    private String content;

    // Getter와 Setter
    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }
  }

  // 댓글 응답 DTO
  public static class Response {
    private Long id;
    private Long postId;
    private UserDto.SimpleResponse user;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 생성자
    public Response() {
    }

    public Response(Comment comment) {
      this.id = comment.getId();
      this.postId = comment.getPost().getId();
      this.user = new UserDto.SimpleResponse(comment.getUser());
      this.content = comment.getContent();
      this.createdAt = comment.getCreatedAt();
      this.updatedAt = comment.getUpdatedAt();
    }

    // Getter와 Setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Long getPostId() {
      return postId;
    }

    public void setPostId(Long postId) {
      this.postId = postId;
    }

    public UserDto.SimpleResponse getUser() {
      return user;
    }

    public void setUser(UserDto.SimpleResponse user) {
      this.user = user;
    }

    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
      return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
    }
  }
}
