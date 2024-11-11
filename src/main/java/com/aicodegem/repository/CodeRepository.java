package com.aicodegem.repository;

import com.aicodegem.model.CodeSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends MongoRepository<CodeSubmission, String> {
    Optional<CodeSubmission> findByUserId(Long userId); // 반환 타입이 Optional이어야 합니다.

    List<CodeSubmission> findAllByUserId(Long userId);
}
