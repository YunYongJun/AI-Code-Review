package com.aicodegem.service;

import com.aicodegem.model.Achievement;
import com.aicodegem.model.UserAchievement;

import java.util.List;

public interface AchievementService {
    /** 전체 업적목록 가져오기 */
    List<Achievement> getAllAchievements();
    /** 업적 id에 해당하는 업적 가져오기   */
    Achievement getAchievementById(Long achievementId);
    /** 누적 점수 업적 업데이트 */
    void assignAchievementsByTotalScore(Long userId);
    /** 사용자 업적 저장 */
    UserAchievement saveUserAchievement(UserAchievement userAchievement);
    /** 베이스 업적 저장 */
    Achievement saveAchievement(Achievement achievement);

}
