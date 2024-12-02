package com.aicodegem.service;

import com.aicodegem.model.UserAchievement;

import java.util.List;

public interface UserAchievementService {
    /** 특정 사용자의 업적 조회 */
    List<UserAchievement> getAchievementsByUserId(Long userId);

    /** 이미 사용자가 깬 업적인지 확인 */
    boolean isAchievementAlreadyAssigned(Long userId, Long achievementId);

    /** 특정 사용자의 업적 달성을 저장 */
    UserAchievement assignAchievementToUser(UserAchievement userAchievement);
}
