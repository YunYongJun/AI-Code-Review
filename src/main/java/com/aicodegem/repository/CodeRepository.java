package com.aicodegem.repository;

import com.aicodegem.model.CodeSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CodeRepository extends MongoRepository<CodeSubmission, String> {
    // 사용자 ID로 코드 제출 기록 조회
    CodeSubmission findByUserId(Long userId);
}
