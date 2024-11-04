package com.aicodegem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.aicodegem.model.Ranking;
import com.aicodegem.service.RankingService;
import com.aicodegem.service.RankingServiceImpl;

// Ranking API를 제공하는 컨트롤러 클래스
@RestController
@RequestMapping("/api/rankings") // 기본 URL 매핑
public class RankingController {

    @Autowired
    private RankingService rankingService; // RankingService 의존성 주입

    // 사용자 ID로 순위를 조회하는 GET 요청 처리
    @GetMapping("/{userId}")
    public ResponseEntity<Ranking> getRanking(@PathVariable Long userId) {
        Ranking ranking = rankingService.getRankingByUserId(userId); // 순위 조회
        return ResponseEntity.ok(ranking); // 성공 응답 반환
    }

    // 순위를 저장하는 POST 요청 처리
    @PostMapping
    public ResponseEntity<Ranking> createRanking(@RequestBody Ranking ranking) {
        Ranking savedRanking = rankingService.saveRanking(ranking); // 순위 저장
        return ResponseEntity.ok(savedRanking); // 성공 응답 반환
    }

    // 모든 랭킹 정보를 조회하는 GET 요청 처리
    @GetMapping
    public ResponseEntity<List<Ranking>> getAllRankings() {
        List<Ranking> rankings = rankingService.getAllRankings(); // 모든 랭킹 조회
        return ResponseEntity.ok(rankings); // 성공 응답 반환
    }

    // 사용자 ID로 username을 조회하는 GET 요청 처리
    @GetMapping("/username/{userId}")
    public ResponseEntity<String> getUsernameByUserId(@PathVariable Long userId) {
        String username = ((RankingServiceImpl) rankingService).getUsernameByUserId(userId);
        return username != null ? ResponseEntity.ok(username) : ResponseEntity.notFound().build();
    }
}
