package com.aicodegem.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;

@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    private static final Logger logger = LoggerFactory.getLogger(CodeAnalysisController.class); // Logger 생성

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    // 특정 사용자의 모든 제출 코드 조회 API
    @GetMapping("/submissions")
    public List<CodeSubmission> getSubmissions(@RequestParam("userId") Long userId) {
        logger.info("사용자 ID: {}의 모든 코드 제출을 조회합니다.", userId);
        List<CodeSubmission> submissions = codeSubmissionService.getAllSubmissionsByUserId(userId);
        logger.info("사용자 ID: {}의 제출 코드 개수: {}", userId, submissions.size()); // 제출된 코드 개수 로그
        return submissions;
    }

    // 특정 제출 코드 조회 API
    @GetMapping("/submissions/{submissionId}")
    public CodeSubmission getSubmissionById(@PathVariable("submissionId") String submissionId) {
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
    public ResponseEntity<String> submitCode(@RequestBody CodeSubmissionRequest request) {
        logger.info("사용자 ID: {}의 코드 제출 요청이 들어왔습니다. 제목: {}", request.getUserId(), request.getTitle());
        try {
            CodeSubmission submission = codeSubmissionService.submitCode(
                    request.getUserId(),
                    request.getCode(),
                    request.getTitle() // title 필드 처리
            );
            logger.info("코드 제출 성공: 제출 ID = {}", submission.getId()); // 성공 로그
            return ResponseEntity.ok("코드 제출 성공, 제출 ID: " + submission.getId()); // 성공 메시지 반환
        } catch (IOException e) {
            logger.error("코드 제출 중 오류 발생: {}", e.getMessage(), e); // 오류 로그
            return ResponseEntity.status(500).body("코드 제출 중 오류 발생: " + e.getMessage()); // 오류 메시지와 함께 500 반환
        }
    }

    // 코드 수정
    @PutMapping("/submit/revised")
    public ResponseEntity<CodeSubmission> updateRevisedCode(@RequestBody Map<String, String> request) {
        String submissionId = request.get("submissionId");
        String revisedCode = request.get("revisedCode");

        try {
            CodeSubmission updatedSubmission = codeSubmissionService.analyzeAndStoreRevisedCode(submissionId,
                    revisedCode);
            return ResponseEntity.ok(updatedSubmission);
        } catch (IOException e) {
            throw new RuntimeException("수정된 코드 제출 중 오류 발생: " + e.getMessage());
        }
    }
}
