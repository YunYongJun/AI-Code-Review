package com.aicodegem.repository;

import com.aicodegem.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Ranking 엔티티에 대한 CRUD 및 사용자 정의 쿼리를 제공하는 리포지토리 인터페이스
public interface RankingRepository extends JpaRepository<Ranking, Integer> {

    Optional<Ranking> findByUser_Id(Long userId); // userId로 Ranking 조회
}
