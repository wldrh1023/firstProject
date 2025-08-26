package com.connecthub.socialmedia.repository;

import com.connecthub.socialmedia.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
  Page<Post> findByUserIdsOrderByCreatedAtDesc(@Param("userIds") List<Long> userIds, Pageable pageable);

  @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% ORDER BY p.createdAt DESC")
  Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :userId")
  long countByUserId(@Param("userId") Long userId);

  @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.likes LEFT JOIN FETCH p.comments ORDER BY p.createdAt DESC")
  Page<Post> findAllWithUserAndStats(Pageable pageable);

  @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user WHERE p.id = :postId")
  Post findByIdWithUser(@Param("postId") Long postId);
}
