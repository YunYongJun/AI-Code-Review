package com.aicodegem.controller;

import com.aicodegem.model.Achievement;
import com.aicodegem.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Achievement API를 제공하는 컨트롤러 클래스
@RestController
@RequestMapping("/api/achievements") // 기본 URL 매핑
public class AchievementController {

    @Autowired
    private AchievementService achievementService; // AchievementService 의존성 주입

    // 사용자 ID로 업적을 조회하는 GET 요청 처리
    @GetMapping("/{userId}")
    public ResponseEntity<List<Achievement>> getAchievements(@PathVariable Long userId) {

        // 주어진 사용자 ID로 업적 조회
        List<Achievement> achievements = achievementService.getAchievementsByUserId(userId);
        return ResponseEntity.ok(achievements); // 성공 응답 반환
    }

    // 업적을 저장하는 POST 요청 처리
    @PostMapping
    public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
        // 업적 저장
        Achievement savedAchievement = achievementService.saveAchievement(achievement);
        return ResponseEntity.ok(savedAchievement); // 성공 응답 반환
    }
}
