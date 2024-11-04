package com.aicodegem.repository;

import com.aicodegem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { // Long 타입의 기본 키
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    // user_id로 user_name 찾기
    @Query("SELECT u.username FROM User u WHERE u.id = :userId")
    Optional<String> findUsernameById(@Param("userId") Long userId);
}
