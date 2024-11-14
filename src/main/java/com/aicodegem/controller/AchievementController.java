package com.aicodegem.controller;

import com.aicodegem.model.Achievement;
import com.aicodegem.service.AchievementService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

// Achievement API를 제공하는 컨트롤러 클래스
@RestController
@RequestMapping("/api/achievements") // 기본 URL 매핑
public class AchievementController {

    private static final Logger logger = LoggerFactory.getLogger(AchievementController.class); // Logger 생성

    @Autowired
    private AchievementService achievementService; // AchievementService 의존성 주입

    // 사용자 ID로 업적을 조회하는 GET 요청 처리
    @GetMapping("/{userId}")
    public ResponseEntity<List<Achievement>> getAchievements(@PathVariable("userId") Long userId) {

        // 업적 조회 메서드 호출 시 사용자 ID를 로깅
        logger.info("getAchievements 호출됨 - userId: {}", userId);

        // 주어진 사용자 ID로 업적 조회
        List<Achievement> achievements = achievementService.getAchievementsByUserId(userId);

        // 업적이 발견되지 않은 경우 경고 로깅
        if (achievements.isEmpty()) {
            logger.warn("업적이 발견되지 않음 - userId: {}", userId);
        } else {
            // 업적이 존재하는 경우 성공 로깅
            logger.info("업적 조회 성공 - userId: {}, 업적 수: {}", userId, achievements.size());
        }

        return ResponseEntity.ok(achievements); // 조회된 업적 목록을 응답으로 반환
    }

    // 업적을 저장하는 POST 요청 처리
    @PostMapping
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {

        // 업적 생성 요청 시 입력 데이터를 로깅
        logger.info("createAchievement 호출됨 - Achievement: {}", achievement);

        // 업적 저장
        Achievement savedAchievement = achievementService.saveAchievement(achievement);

        // 업적이 성공적으로 저장되었음을 로깅
        logger.info("업적 저장 성공 - Achievement ID: {}", savedAchievement.getId());

        return ResponseEntity.ok(savedAchievement); // 저장된 업적을 응답으로 반환
    }
}
