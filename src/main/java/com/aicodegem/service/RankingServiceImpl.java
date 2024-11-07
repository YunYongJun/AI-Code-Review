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
    public Ranking getRankingByUserId(Long userId) {
        return rankingRepository.findByUserId(userId).orElse(null); // 사용자 ID로 순위 조회
    }

    @Override
    public Ranking saveRanking(Ranking ranking) {
        return rankingRepository.save(ranking); // 순위 저장
    }

    @Override
    public List<Ranking> getAllRankings() {
        return rankingRepository.findAll(); // 모든 랭킹 정보 조회
    }

    // 점수를 업데이트하는 메서드
    @Override
    public void updateTotalScore(Long userId, int newScore, int previousScore) {
        Ranking ranking = rankingRepository.findByUserId(userId).orElse(null);

        if (ranking != null) {
            // 기존 점수를 빼고 새로운 점수를 추가하여 총점을 업데이트
            int updatedScore = ranking.getTotalScore() - previousScore + newScore;
            ranking.setTotalScore(updatedScore);
            rankingRepository.save(ranking);
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
        }
    }

}
