package com.aicodegem.service;

import com.aicodegem.model.Ranking;
import com.aicodegem.repository.RankingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

// RankingService 인터페이스의 구현 클래스
@Service
public class RankingServiceImpl implements RankingService {

    private static final Logger logger = LoggerFactory.getLogger(RankingServiceImpl.class);

    @Autowired
    private RankingRepository rankingRepository; // RankingRepository 의존성 주입

    @Override
    public Ranking getRankingByUserId(Long userId) {
        logger.info("사용자 ID {}로 순위 조회 요청을 받았습니다.", userId);
        Ranking ranking = rankingRepository.findByUser_Id(userId).orElse(null); // 사용자 ID로 순위 조회

        if (ranking != null) {
            logger.info("순위 조회 성공 - 사용자 ID: {}, 순위: {}", userId, ranking.getUserRank());
        } else {
            logger.warn("순위 조회 실패 - 사용자 ID {}에 대한 순위가 없습니다.", userId);
        }

        return ranking;
    }

    @Override
    public Ranking saveRanking(Ranking ranking) {
        logger.info("새 순위를 저장합니다 - 사용자 ID: {}, 순위: {}", ranking.getUser().getId(), ranking.getUserRank());
        Ranking savedRanking = rankingRepository.save(ranking); // 순위 저장
        logger.info("순위 저장 성공 - ID: {}", savedRanking.getId());
        return savedRanking;
    }

    @Override
    public List<Ranking> getAllRankings() {
        logger.info("모든 랭킹 정보를 조회합니다.");
        List<Ranking> rankings = rankingRepository.findAll(); // 모든 랭킹 정보 조회
        logger.info("총 {}개의 랭킹 정보를 조회했습니다.", rankings.size());
        return rankings;
    }

    // 점수를 업데이트하는 메서드
    @Override
    public void updateTotalScore(Long userId, int newScore, int previousScore) {
        logger.info("사용자 ID {}의 총점 업데이트 요청 - 새로운 점수: {}, 이전 점수: {}", userId, newScore, previousScore);
        Ranking ranking = rankingRepository.findByUser_Id(userId).orElse(null);

        if (ranking != null) {
            // 기존 점수를 빼고 새로운 점수를 추가하여 총점을 업데이트
            int updatedScore = ranking.getTotalScore() - previousScore + newScore;
            ranking.setTotalScore(updatedScore);
            rankingRepository.save(ranking);
            logger.info("총점 업데이트 성공 - 사용자 ID: {}, 새로운 총점: {}", userId, updatedScore);
        } else {
            String errorMessage = "사용자를 찾을 수 없습니다 - 사용자 ID: " + userId;
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

}
