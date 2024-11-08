package com.aicodegem.repository;

import com.aicodegem.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Achievement 엔티티에 대한 CRUD 및 사용자 정의 쿼리를 제공하는 리포지토리 인터페이스
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    // 사용자 ID로 업적 조회
    List<Achievement> findByUserId(Long userId);
}
