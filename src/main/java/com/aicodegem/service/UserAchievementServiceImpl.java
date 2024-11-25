package com.aicodegem.service;

import com.aicodegem.model.UserAchievement;
import com.aicodegem.repository.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAchievementServiceImpl implements UserAchievementService {

    @Autowired
    private UserAchievementRepository userAchievementRepository;

    @Override
    public List<UserAchievement> getAchievementsByUserId(Long userId) {
        return userAchievementRepository.findByUser_Id(userId);
    }

    @Override
    public boolean isAchievementAlreadyAssigned(Long userId, Long achievementId) {
        return userAchievementRepository.existsByUser_IdAndAchievement_Id(userId, achievementId);
    }

    @Override
    public UserAchievement assignAchievementToUser(UserAchievement userAchievement) {
        return userAchievementRepository.save(userAchievement);
    }
}
