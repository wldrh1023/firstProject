package com.connecthub.socialmedia.repository;

import com.connecthub.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
  List<User> searchByKeyword(@Param("keyword") String keyword);

  @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :userId")
  long countFollowers(@Param("userId") Long userId);

  @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :userId")
  long countFollowing(@Param("userId") Long userId);

  @Query("SELECT u FROM User u JOIN Follow f ON u.id = f.following.id WHERE f.follower.id = :userId")
  List<User> findFollowingUsers(@Param("userId") Long userId);

  @Query("SELECT u FROM User u JOIN Follow f ON u.id = f.follower.id WHERE f.following.id = :userId")
  List<User> findFollowers(@Param("userId") Long userId);
}
