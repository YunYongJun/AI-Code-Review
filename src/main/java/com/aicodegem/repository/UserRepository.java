package com.aicodegem.repository;

import com.aicodegem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> { // Long 타입의 기본 키
    /**사용자 이름으로 사용자 조회 */
    Optional<User> findByUsername(String username);
    /** 사용자 이름으로 사용자가 존재하는지 확인 */
    boolean existsByUsername(String username);
}
