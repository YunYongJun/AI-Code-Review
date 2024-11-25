package com.aicodegem.controller;

import com.aicodegem.model.Achievement;
import com.aicodegem.model.UserAchievement;
import com.aicodegem.service.AchievementService;
import com.aicodegem.service.UserAchievementService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List; //주석

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private static final Logger logger = LoggerFactory.getLogger(AchievementController.class);

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private UserAchievementService userAchievementService;

    // **1. 모든 업적 조회 (Achievement 테이블)**
    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        logger.info("getAllAchievements 호출됨");
        List<Achievement> achievements = achievementService.getAllAchievements();

        if (achievements.isEmpty()) {
            logger.warn("업적 목록이 비어있음");
        } else {
            logger.info("업적 조회 성공 - 업적 수: {}", achievements.size());
        }

        return ResponseEntity.ok(achievements);
    }

    // **2. 사용자별 업적 조회 (UserAchievement 테이블)**
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserAchievement>> getUserAchievements(@PathVariable("userId") Long userId) {
        logger.info("getUserAchievements 호출됨 - userId: {}", userId);
        List<UserAchievement> userAchievements = userAchievementService.getAchievementsByUserId(userId);

        if (userAchievements.isEmpty()) {
            logger.warn("사용자의 업적이 발견되지 않음 - userId: {}", userId);
        } else {
            logger.info("사용자 업적 조회 성공 - userId: {}, 업적 수: {}", userId, userAchievements.size());
        }

        return ResponseEntity.ok(userAchievements);
    }

    // **3. 새 업적 저장 (Achievement 테이블)**
    @PostMapping("/achievement")
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
        // 업적 생성 요청 시 입력 데이터를 로깅
        logger.info("createAchievement 호출됨 - Achievement: {}", achievement);

        // Achievement 저장
        Achievement savedAchievement = achievementService.saveAchievement(achievement);

        // 업적이 성공적으로 저장되었음을 로깅
        logger.info("업적 저장 성공 - Achievement ID: {}", savedAchievement.getId());

        return ResponseEntity.ok(savedAchievement); // 저장된 업적을 응답으로 반환
    }
}
