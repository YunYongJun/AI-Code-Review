package com.aicodegem.service;

import com.aicodegem.model.Achievement;
import com.aicodegem.model.UserAchievement;

import java.util.List;

public interface AchievementService {
    List<Achievement> getAllAchievements();

    Achievement getAchievementById(Long achievementId);

    void assignAchievementsByTotalScore(Long userId);

    UserAchievement saveUserAchievement(UserAchievement userAchievement);

    Achievement saveAchievement(Achievement achievement);

}
