package com.connecthub.socialmedia.service;

import com.connecthub.socialmedia.dto.PostDto;
import com.connecthub.socialmedia.entity.Post;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.repository.FollowRepository;
import com.connecthub.socialmedia.repository.LikeRepository;
import com.connecthub.socialmedia.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private LikeRepository likeRepository;

  @Autowired
  private FollowRepository followRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private NotificationService notificationService;

  public Post createPost(Long userId, PostDto.CreateRequest createRequest) {
    User user = userService.getUserById(userId);

    Post post = new Post();
    post.setUser(user);
    post.setContent(createRequest.getContent());
    post.setImageUrl(createRequest.getImageUrl());

    return postRepository.save(post);
  }

  @Transactional(readOnly = true)
  public Post getPostById(Long postId) {
    return postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public PostDto.Response getPostResponse(Long postId, Long currentUserId) {
    Post post = postRepository.findByIdWithUser(postId);
    if (post == null) {
      throw new RuntimeException("게시물을 찾을 수 없습니다.");
    }

    PostDto.Response response = new PostDto.Response(post);

    // 좋아요 수와 댓글 수 설정
    response.setLikesCount((int) likeRepository.countByPostId(postId));
    response.setCommentsCount((int) postRepository.countByUserId(postId));

    // 현재 사용자가 좋아요를 눌렀는지 확인
    if (currentUserId != null) {
      response.setLiked(likeRepository.existsByPostIdAndUserId(postId, currentUserId));
    }

    return response;
  }

  @Transactional(readOnly = true)
  public Page<PostDto.SimpleResponse> getFeed(Long userId, Pageable pageable) {
    // 팔로우하는 사용자들의 ID 가져오기
    List<Long> followingUserIds = followRepository.findFollowingUserIds(userId);
    followingUserIds.add(userId); // 자신의 게시물도 포함

    Page<Post> posts = postRepository.findByUserIdsOrderByCreatedAtDesc(followingUserIds, pageable);

    return posts.map(post -> {
      PostDto.SimpleResponse response = new PostDto.SimpleResponse(post);
      response.setLikesCount((int) likeRepository.countByPostId(post.getId()));
      response.setLiked(likeRepository.existsByPostIdAndUserId(post.getId(), userId));
      return response;
    });
  }

  @Transactional(readOnly = true)
  public Page<PostDto.SimpleResponse> getUserPosts(Long userId, Long currentUserId, Pageable pageable) {
    Page<Post> posts = postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

    return posts.map(post -> {
      PostDto.SimpleResponse response = new PostDto.SimpleResponse(post);
      response.setLikesCount((int) likeRepository.countByPostId(post.getId()));

      if (currentUserId != null) {
        response.setLiked(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
      }

      return response;
    });
  }

  @Transactional(readOnly = true)
  public Page<PostDto.SimpleResponse> searchPosts(String keyword, Long currentUserId, Pageable pageable) {
    Page<Post> posts = postRepository.searchByKeyword(keyword, pageable);

    return posts.map(post -> {
      PostDto.SimpleResponse response = new PostDto.SimpleResponse(post);
      response.setLikesCount((int) likeRepository.countByPostId(post.getId()));

      if (currentUserId != null) {
        response.setLiked(likeRepository.existsByPostIdAndUserId(post.getId(), currentUserId));
      }

      return response;
    });
  }

  public Post updatePost(Long postId, Long userId, PostDto.UpdateRequest updateRequest) {
    Post post = getPostById(postId);

    // 작성자 확인
    if (!post.getUser().getId().equals(userId)) {
      throw new RuntimeException("게시물을 수정할 권한이 없습니다.");
    }

    post.setContent(updateRequest.getContent());
    post.setImageUrl(updateRequest.getImageUrl());

    return postRepository.save(post);
  }

  public void deletePost(Long postId, Long userId) {
    Post post = getPostById(postId);

    // 작성자 확인
    if (!post.getUser().getId().equals(userId)) {
      throw new RuntimeException("게시물을 삭제할 권한이 없습니다.");
    }

    postRepository.delete(post);
  }
}
