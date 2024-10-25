package com.aicodegem.service;

import com.aicodegem.model.Ranking;

// Ranking 관련 비즈니스 로직을 정의하는 서비스 인터페이스
public interface RankingService {
    Ranking getRankingByUserId(int userId); // 사용자 ID로 순위 조회

    Ranking saveRanking(Ranking ranking); // 순위 저장
}
