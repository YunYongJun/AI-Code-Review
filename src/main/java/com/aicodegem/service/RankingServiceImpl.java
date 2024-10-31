package com.aicodegem.service;

import com.aicodegem.model.Ranking;
import com.aicodegem.repository.RankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// RankingService 인터페이스의 구현 클래스
@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private RankingRepository rankingRepository; // RankingRepository 의존성 주입

    @Override
    public Ranking getRankingByUserId(int userId) {
        return rankingRepository.findByUserId(userId); // 사용자 ID로 순위 조회
    }

    @Override
    public Ranking saveRanking(Ranking ranking) {
        return rankingRepository.save(ranking); // 순위 저장
    }

    @Override
    public List<Ranking> getAllRankings() {
        return rankingRepository.findAll(); // 모든 랭킹 정보 조회
    }
}
