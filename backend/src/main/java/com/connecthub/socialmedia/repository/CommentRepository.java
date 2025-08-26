package com.connecthub.socialmedia.repository;

import com.connecthub.socialmedia.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

  Page<Comment> findByPostIdOrderByCreatedAtAsc(Long postId, Pageable pageable);

  List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);

  @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
  long countByPostId(@Param("postId") Long postId);

  @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
  List<Comment> findByPostIdWithUser(@Param("postId") Long postId);

  void deleteByPostId(Long postId);
}
