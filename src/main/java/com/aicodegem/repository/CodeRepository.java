package com.aicodegem.repository;

import com.aicodegem.model.CodeSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CodeRepository extends MongoRepository<CodeSubmission, String> {
    Optional<CodeSubmission> findByUserId(Long userId);

    List<CodeSubmission> findAllByUserId(Long userId);
}
