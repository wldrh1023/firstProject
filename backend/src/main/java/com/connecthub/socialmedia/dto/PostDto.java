package com.connecthub.socialmedia.dto;

import com.connecthub.socialmedia.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public class PostDto {

  // 게시물 생성 요청 DTO
  public static class CreateRequest {
    @NotBlank(message = "게시물 내용은 필수입니다")
    @Size(max = 2000, message = "게시물 내용은 2000자를 초과할 수 없습니다")
    private String content;

    @Size(max = 500, message = "이미지 URL은 500자를 초과할 수 없습니다")
    private String imageUrl;

    // Getter와 Setter
    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }
  }

  // 게시물 수정 요청 DTO
  public static class UpdateRequest {
    @NotBlank(message = "게시물 내용은 필수입니다")
    @Size(max = 2000, message = "게시물 내용은 2000자를 초과할 수 없습니다")
    private String content;

    @Size(max = 500, message = "이미지 URL은 500자를 초과할 수 없습니다")
    private String imageUrl;

    // Getter와 Setter
    public String getContent() {
      return content;
    }

    public void setContent(String content) {
      this.content = content;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }
  }

  // 게시물 응답 DTO
  public static class Response {
    private Long id;
    private UserDto.SimpleResponse user;
    private String content;
    private String imageUrl;
    private int likesCount;
    private int commentsCount;
    private boolean isLiked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto.Response> comments;

    // 생성자
    public Response() {
    }

    public Response(Post post) {
      this.id = post.getId();
      this.user = new UserDto.SimpleResponse(post.getUser());
      this.content = post.getContent();
      this.imageUrl = post.getImageUrl();
      this.likesCount = post.getLikeCount();
      this.commentsCount = post.getCommentCount();
      this.createdAt = post.getCreatedAt();
      this.updatedAt = post.getUpdatedAt();
    }

    // Getter와 Setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
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

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }

    public int getLikesCount() {
      return likesCount;
    }

    public void setLikesCount(int likesCount) {
      this.likesCount = likesCount;
    }

    public int getCommentsCount() {
      return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
      this.commentsCount = commentsCount;
    }

    public boolean isLiked() {
      return isLiked;
    }

    public void setLiked(boolean liked) {
      isLiked = liked;
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

    public List<CommentDto.Response> getComments() {
      return comments;
    }

    public void setComments(List<CommentDto.Response> comments) {
      this.comments = comments;
    }
  }

  // 간단한 게시물 응답 DTO (목록용)
  public static class SimpleResponse {
    private Long id;
    private UserDto.SimpleResponse user;
    private String content;
    private String imageUrl;
    private int likesCount;
    private int commentsCount;
    private boolean isLiked;
    private LocalDateTime createdAt;

    public SimpleResponse() {
    }

    public SimpleResponse(Post post) {
      this.id = post.getId();
      this.user = new UserDto.SimpleResponse(post.getUser());
      this.content = post.getContent();
      this.imageUrl = post.getImageUrl();
      this.likesCount = post.getLikeCount();
      this.commentsCount = post.getCommentCount();
      this.createdAt = post.getCreatedAt();
    }

    // Getter와 Setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
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

    public String getImageUrl() {
      return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
    }

    public int getLikesCount() {
      return likesCount;
    }

    public void setLikesCount(int likesCount) {
      this.likesCount = likesCount;
    }

    public int getCommentsCount() {
      return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
      this.commentsCount = commentsCount;
    }

    public boolean isLiked() {
      return isLiked;
    }

    public void setLiked(boolean liked) {
      isLiked = liked;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }
  }
}
