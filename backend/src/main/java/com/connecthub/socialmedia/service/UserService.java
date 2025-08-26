package com.connecthub.socialmedia.service;

import com.connecthub.socialmedia.dto.UserDto;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.repository.FollowRepository;
import com.connecthub.socialmedia.repository.PostRepository;
import com.connecthub.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FollowRepository followRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User createUser(UserDto.SignUpRequest signUpRequest) {
    // 중복 체크
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new RuntimeException("이미 사용 중인 사용자명입니다.");
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new RuntimeException("이미 사용 중인 이메일입니다.");
    }

    // 새 사용자 생성
    User user = new User();
    user.setUsername(signUpRequest.getUsername());
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public User getUserById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public UserDto.Response getUserProfile(Long userId, Long currentUserId) {
    User user = getUserById(userId);
    UserDto.Response response = new UserDto.Response(user);

    // 통계 정보 설정
    response.setFollowersCount(followRepository.countFollowers(userId));
    response.setFollowingCount(followRepository.countFollowing(userId));
    response.setPostsCount(postRepository.countByUserId(userId));

    // 팔로우 상태 설정 (현재 사용자가 이 사용자를 팔로우하고 있는지)
    if (currentUserId != null && !currentUserId.equals(userId)) {
      response.setFollowing(followRepository.existsByFollowerIdAndFollowingId(currentUserId, userId));
    }

    return response;
  }

  public User updateProfile(Long userId, UserDto.ProfileUpdateRequest updateRequest) {
    User user = getUserById(userId);

    if (updateRequest.getProfileImageUrl() != null) {
      user.setProfileImageUrl(updateRequest.getProfileImageUrl());
    }

    if (updateRequest.getBio() != null) {
      user.setBio(updateRequest.getBio());
    }

    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public List<UserDto.Response> searchUsers(String keyword, Long currentUserId) {
    List<User> users = userRepository.searchByKeyword(keyword);

    return users.stream()
        .map(user -> {
          UserDto.Response response = new UserDto.Response(user);
          response.setFollowersCount(followRepository.countFollowers(user.getId()));
          response.setFollowingCount(followRepository.countFollowing(user.getId()));
          response.setPostsCount(postRepository.countByUserId(user.getId()));

          if (currentUserId != null && !currentUserId.equals(user.getId())) {
            response.setFollowing(followRepository.existsByFollowerIdAndFollowingId(currentUserId, user.getId()));
          }

          return response;
        })
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<UserDto.SimpleResponse> getFollowers(Long userId) {
    List<User> followers = userRepository.findFollowers(userId);
    return followers.stream()
        .map(UserDto.SimpleResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<UserDto.SimpleResponse> getFollowing(Long userId) {
    List<User> following = userRepository.findFollowingUsers(userId);
    return following.stream()
        .map(UserDto.SimpleResponse::new)
        .collect(Collectors.toList());
  }
}
