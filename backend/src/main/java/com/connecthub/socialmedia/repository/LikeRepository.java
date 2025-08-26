package com.connecthub.socialmedia.repository;

import com.connecthub.socialmedia.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

  Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

  boolean existsByPostIdAndUserId(Long postId, Long userId);

  void deleteByPostIdAndUserId(Long postId, Long userId);

  List<Like> findByPostIdOrderByCreatedAtDesc(Long postId);

  List<Like> findByUserIdOrderByCreatedAtDesc(Long userId);

  @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
  long countByPostId(@Param("postId") Long postId);

  @Query("SELECT l FROM Like l LEFT JOIN FETCH l.user WHERE l.post.id = :postId ORDER BY l.createdAt DESC")
  List<Like> findByPostIdWithUser(@Param("postId") Long postId);

  void deleteByPostId(Long postId);
}
