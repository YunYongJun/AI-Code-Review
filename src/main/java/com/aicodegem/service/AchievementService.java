package com.aicodegem.service;

import com.aicodegem.model.Achievement;

import java.util.List;

// Achievement 관련 비즈니스 로직을 정의하는 서비스 인터페이스
public interface AchievementService {
    List<Achievement> getAchievementsByUserId(int userId); // 사용자 ID로 업적 조회

    Achievement saveAchievement(Achievement achievement); // 업적 저장
}
