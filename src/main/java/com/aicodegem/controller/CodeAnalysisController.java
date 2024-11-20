package com.aicodegem.controller;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(CodeAnalysisController.class); // Logger 생성

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    // 특정 사용자의 모든 제출 코드 조회 API
    @GetMapping("/submissions")
    public List<CodeSubmission> getSubmissions(@RequestParam Long userId) {
        logger.info("사용자 ID: {}의 모든 코드 제출을 조회합니다.", userId);
        List<CodeSubmission> submissions = codeSubmissionService.getAllSubmissionsByUserId(userId);
        logger.info("사용자 ID: {}의 제출 코드 개수: {}", userId, submissions.size()); // 제출된 코드 개수 로그
        return submissions;
    }

    // 특정 제출 코드 조회 API
    @GetMapping("/submissions/{submissionId}")
    public CodeSubmission getSubmissionById(@PathVariable String submissionId) {
        logger.info("제출 코드 조회 요청: submissionId={}", submissionId);
        CodeSubmission submission = codeSubmissionService.getSubmissionById(submissionId);
        if (submission != null) {
            logger.info("제출 코드 조회 성공: {}", submissionId); // 성공 로그
        } else {
            logger.warn("제출 코드 조회 실패: {} - 해당 ID의 제출 코드가 존재하지 않습니다.", submissionId); // 실패 로그
        }
        return submission;
    }

    // 코드 제출 API - AI 분석 후 저장
    @PostMapping("/submit")
    public ResponseEntity<CodeSubmission> submitCode(@RequestBody CodeSubmissionRequest request) {
        logger.info("사용자 ID: {}의 코드 제출 요청이 들어왔습니다. 제목: {}", request.getUserId(), request.getTitle());
        try {
            CodeSubmission submission = codeSubmissionService.submitCode(
                    request.getUserId(),
                    request.getCode(),
                    request.getTitle() // title 필드 처리
            );
            logger.info("코드 제출 성공: 제출 ID = {}", submission.getId()); // 성공 로그
            return ResponseEntity.ok(submission);
        } catch (IOException e) {
            logger.error("코드 제출 중 오류 발생: {}", e.getMessage(), e); // 오류 로그
            throw new RuntimeException("코드 제출 중 오류 발생: " + e.getMessage());
        }
    }
}
