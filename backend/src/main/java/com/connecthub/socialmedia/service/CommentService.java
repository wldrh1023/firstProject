package com.connecthub.socialmedia.service;

import com.connecthub.socialmedia.dto.CommentDto;
import com.connecthub.socialmedia.entity.Comment;
import com.connecthub.socialmedia.entity.Post;
import com.connecthub.socialmedia.entity.User;
import com.connecthub.socialmedia.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostService postService;

  @Autowired
  private UserService userService;

  @Autowired
  private NotificationService notificationService;

  public Comment createComment(Long postId, Long userId, CommentDto.CreateRequest createRequest) {
    Post post = postService.getPostById(postId);
    User user = userService.getUserById(userId);

    Comment comment = new Comment();
    comment.setPost(post);
    comment.setUser(user);
    comment.setContent(createRequest.getContent());

    Comment savedComment = commentRepository.save(comment);

    // 자신의 게시물이 아닌 경우에만 알림 생성
    if (!post.getUser().getId().equals(userId)) {
      String message = user.getUsername() + "님이 회원님의 게시물에 댓글을 달았습니다: " + createRequest.getContent();
      notificationService.createNotification(
          post.getUser().getId(),
          "COMMENT",
          message);
    }

    return savedComment;
  }

  @Transactional(readOnly = true)
  public Comment getCommentById(Long commentId) {
    return commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
  }

  @Transactional(readOnly = true)
  public List<CommentDto.Response> getCommentsByPostId(Long postId) {
    List<Comment> comments = commentRepository.findByPostIdWithUser(postId);

    return comments.stream()
        .map(CommentDto.Response::new)
        .collect(Collectors.toList());
  }

  public Comment updateComment(Long commentId, Long userId, CommentDto.UpdateRequest updateRequest) {
    Comment comment = getCommentById(commentId);

    // 작성자 확인
    if (!comment.getUser().getId().equals(userId)) {
      throw new RuntimeException("댓글을 수정할 권한이 없습니다.");
    }

    comment.setContent(updateRequest.getContent());

    return commentRepository.save(comment);
  }

  public void deleteComment(Long commentId, Long userId) {
    Comment comment = getCommentById(commentId);

    // 작성자 또는 게시물 작성자 확인
    if (!comment.getUser().getId().equals(userId) &&
        !comment.getPost().getUser().getId().equals(userId)) {
      throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
    }

    commentRepository.delete(comment);
  }

  @Transactional(readOnly = true)
  public long getCommentCount(Long postId) {
    return commentRepository.countByPostId(postId);
  }
}
