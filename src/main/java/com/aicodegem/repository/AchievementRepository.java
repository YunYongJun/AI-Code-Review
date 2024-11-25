package com.aicodegem.repository;

import com.aicodegem.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    // 업적 이름으로 조회 (예시)
    Achievement findByAchievementName(String achievementName);
}
