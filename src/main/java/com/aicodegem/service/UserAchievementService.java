package com.aicodegem.service;

import com.aicodegem.model.UserAchievement;

import java.util.List;

public interface UserAchievementService {
    List<UserAchievement> getAchievementsByUserId(Long userId);

    boolean isAchievementAlreadyAssigned(Long userId, Long achievementId);

    UserAchievement assignAchievementToUser(UserAchievement userAchievement);
}
