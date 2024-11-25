package com.aicodegem.repository;

import com.aicodegem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { // Long 타입의 기본 키
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
