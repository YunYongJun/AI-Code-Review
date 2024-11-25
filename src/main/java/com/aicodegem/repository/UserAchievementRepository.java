package com.aicodegem.repository;

import com.aicodegem.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    // 특정 사용자의 업적 조회
    List<UserAchievement> findByUser_Id(Long userId);

    // 특정 사용자와 업적 간 관계가 존재하는지 확인
    boolean existsByUser_IdAndAchievement_Id(Long userId, Long achievementId);
}
