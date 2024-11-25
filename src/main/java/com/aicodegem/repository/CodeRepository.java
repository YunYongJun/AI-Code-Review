package com.aicodegem.repository;

import com.aicodegem.model.CodeSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends MongoRepository<CodeSubmission, String> {

    // 사용자 ID로 모든 코드 제출을 조회
    List<CodeSubmission> findAllByUserId(Long userId);

    // 특정 제출 ID로 코드 제출을 조회
    Optional<CodeSubmission> findById(String submissionId);

    // 사용자 ID로 특정 코드 제출을 조회 (단일 항목)
    Optional<CodeSubmission> findByUserId(Long userId);
}
