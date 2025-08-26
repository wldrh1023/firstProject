package com.connecthub.socialmedia.repository;

import com.connecthub.socialmedia.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

  Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

  boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

  void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

  List<Follow> findByFollowerIdOrderByCreatedAtDesc(Long followerId);

  List<Follow> findByFollowingIdOrderByCreatedAtDesc(Long followingId);

  @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
  long countFollowers(@Param("userId") Long userId);

  @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
  long countFollowing(@Param("userId") Long userId);

  @Query("SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId")
  List<Long> findFollowingUserIds(@Param("userId") Long userId);

  @Query("SELECT f FROM Follow f LEFT JOIN FETCH f.following WHERE f.follower.id = :userId ORDER BY f.createdAt DESC")
  List<Follow> findByFollowerIdWithFollowing(@Param("userId") Long userId);

  @Query("SELECT f FROM Follow f LEFT JOIN FETCH f.follower WHERE f.following.id = :userId ORDER BY f.createdAt DESC")
  List<Follow> findByFollowingIdWithFollower(@Param("userId") Long userId);
}
