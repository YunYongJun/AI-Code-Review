package com.aicodegem.service;

import com.aicodegem.model.Achievement;
import com.aicodegem.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// AchievementService 인터페이스의 구현 클래스
@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository; // AchievementRepository 의존성 주입

    @Override
    public List<Achievement> getAchievementsByUserId(int userId) {
        return achievementRepository.findByUserId(userId); // 사용자 ID로 업적 조회
    }

    @Override
    public Achievement saveAchievement(Achievement achievement) {
        return achievementRepository.save(achievement); // 업적 저장
    }
}
