package com.aicodegem.service;

import com.aicodegem.model.Achievement;
import com.aicodegem.model.User;
import com.aicodegem.model.UserAchievement;
import com.aicodegem.repository.AchievementRepository;
import com.aicodegem.repository.RankingRepository;
import com.aicodegem.repository.UserAchievementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {

    private static final Logger logger = LoggerFactory.getLogger(AchievementServiceImpl.class);

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private UserAchievementService userAchievementService;

    @Override
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    @Override
    public Achievement getAchievementById(Long achievementId) {
        return achievementRepository.findById(achievementId)
                .orElseThrow(() -> new IllegalArgumentException("Achievement not found: " + achievementId));
    }

    @Override
    public void assignAchievementsByTotalScore(Long userId) {
        logger.info("사용자 ID {}의 누적 점수 기반으로 업적을 확인합니다.", userId);

        int totalScore = rankingRepository.findByUser_Id(userId)
                .map(ranking -> ranking.getTotalScore())
                .orElseThrow(() -> new IllegalArgumentException("사용자의 랭킹 정보가 없습니다."));

        logger.info("사용자의 누적 점수: {}", totalScore);

        List<Achievement> achievements = this.getAllAchievements();

        for (Achievement achievement : achievements) {
            if (evaluateCriteria(achievement.getCriteria(), totalScore)) {
                boolean alreadyAssigned = userAchievementService.isAchievementAlreadyAssigned(userId,
                        achievement.getId());
                if (!alreadyAssigned) {
                    UserAchievement userAchievement = new UserAchievement();
                    // userAchievement.setUser(new User(userId));
                    userAchievement.setAchievement(achievement);
                    userAchievement.setDateAchieved(LocalDate.now());
                    userAchievementService.assignAchievementToUser(userAchievement);

                    logger.info("새로운 업적 '{}'이 사용자 {}에게 할당되었습니다.", achievement.getAchievementName(), userId);
                }
            }
        }
    }

    private boolean evaluateCriteria(String criteria, int totalScore) {
        if (criteria.contains("total_score >= ")) {
            int requiredScore = Integer.parseInt(criteria.split(">= ")[1]);
            return totalScore >= requiredScore;
        }
        return false;
    }

    @Override
    public UserAchievement saveUserAchievement(UserAchievement userAchievement) {
        return userAchievementRepository.save(userAchievement); // UserAchievement 저장
    }

    @Override
    public Achievement saveAchievement(Achievement achievement) {
        return achievementRepository.save(achievement); // Achievement 저장
    }
}
