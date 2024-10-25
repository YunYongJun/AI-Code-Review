package com.aicodegem.repository;

import com.aicodegem.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

// Ranking 엔티티에 대한 CRUD 및 사용자 정의 쿼리를 제공하는 리포지토리 인터페이스
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    // 사용자 ID로 순위 조회
    Ranking findByUserId(int userId);
}
