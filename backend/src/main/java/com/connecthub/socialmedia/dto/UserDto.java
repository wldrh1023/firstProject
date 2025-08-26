package com.connecthub.socialmedia.dto;

import com.connecthub.socialmedia.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class UserDto {

  // 회원가입 요청 DTO
  public static class SignUpRequest {
    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 50, message = "사용자명은 3-50자 사이여야 합니다")
    private String username;

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    private String password;

    // Getter와 Setter
    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  // 로그인 요청 DTO
  public static class LoginRequest {
    @NotBlank(message = "사용자명 또는 이메일은 필수입니다")
    private String usernameOrEmail;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    // Getter와 Setter
    public String getUsernameOrEmail() {
      return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
      this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  // 프로필 업데이트 요청 DTO
  public static class ProfileUpdateRequest {
    @Size(max = 500, message = "프로필 이미지 URL은 500자를 초과할 수 없습니다")
    private String profileImageUrl;

    @Size(max = 1000, message = "자기소개는 1000자를 초과할 수 없습니다")
    private String bio;

    // Getter와 Setter
    public String getProfileImageUrl() {
      return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
      this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
      return bio;
    }

    public void setBio(String bio) {
      this.bio = bio;
    }
  }

  // 사용자 응답 DTO
  public static class Response {
    private Long id;
    private String username;
    private String email;
    private String profileImageUrl;
    private String bio;
    private long followersCount;
    private long followingCount;
    private long postsCount;
    private boolean isFollowing;
    private LocalDateTime createdAt;

    // 생성자
    public Response() {
    }

    public Response(User user) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.email = user.getEmail();
      this.profileImageUrl = user.getProfileImageUrl();
      this.bio = user.getBio();
      this.createdAt = user.getCreatedAt();
    }

    // Getter와 Setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getProfileImageUrl() {
      return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
      this.profileImageUrl = profileImageUrl;
    }

    public String getBio() {
      return bio;
    }

    public void setBio(String bio) {
      this.bio = bio;
    }

    public long getFollowersCount() {
      return followersCount;
    }

    public void setFollowersCount(long followersCount) {
      this.followersCount = followersCount;
    }

    public long getFollowingCount() {
      return followingCount;
    }

    public void setFollowingCount(long followingCount) {
      this.followingCount = followingCount;
    }

    public long getPostsCount() {
      return postsCount;
    }

    public void setPostsCount(long postsCount) {
      this.postsCount = postsCount;
    }

    public boolean isFollowing() {
      return isFollowing;
    }

    public void setFollowing(boolean following) {
      isFollowing = following;
    }

    public LocalDateTime getCreatedAt() {
      return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
    }
  }

  // 간단한 사용자 정보 DTO
  public static class SimpleResponse {
    private Long id;
    private String username;
    private String profileImageUrl;

    public SimpleResponse() {
    }

    public SimpleResponse(User user) {
      this.id = user.getId();
      this.username = user.getUsername();
      this.profileImageUrl = user.getProfileImageUrl();
    }

    // Getter와 Setter
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getProfileImageUrl() {
      return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
      this.profileImageUrl = profileImageUrl;
    }
  }
}
