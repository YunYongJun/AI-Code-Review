package com.aicodegem.service;

import com.aicodegem.model.Achievement;
import com.aicodegem.repository.AchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

// AchievementService 인터페이스의 구현 클래스
@Service
public class AchievementServiceImpl implements AchievementService {

    private static final Logger logger = LoggerFactory.getLogger(AchievementServiceImpl.class);

    @Autowired
    private AchievementRepository achievementRepository; // AchievementRepository 의존성 주입

    @Override
    public List<Achievement> getAchievementsByUserId(Long userId) {
        logger.info("사용자 ID {}의 업적을 조회합니다.", userId); // 업적 조회 로깅
        return achievementRepository.findByUserId(userId); // 사용자 ID로 업적 조회
    }

    @Override
    public Achievement saveAchievement(Achievement achievement) {
        logger.info("새로운 업적을 저장합니다: {}", achievement); // 업적 저장 로깅
        return achievementRepository.save(achievement); // 업적 저장
    }
}
