package com.aicodegem.controller;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    // Logger 생성
    private static final Logger logger = LoggerFactory.getLogger(CodeAnalysisController.class);

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    private static final String AI_SERVER_URL = "http://192.168.34.13:8888/predict"; // AI 서버 URL

    @PostMapping("/submit")
    public ResponseEntity<CodeSubmission> submitCode(@RequestParam(name = "userId") Long userId,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "title") String title) {
        try {
            logger.info("사용자 {}의 코드 제출 시작. 제목: {}", userId, title);
            CodeSubmission submission = codeSubmissionService.submitCode(userId, code, title);
            logger.info("사용자 {}의 코드 제출 완료. 제출 ID: {}", userId, submission.getId());
            return ResponseEntity.ok(submission);
        } catch (IOException | InterruptedException e) {
            logger.error("사용자 {}의 코드 제출 중 오류 발생. 오류: {}", userId, e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/revise")
    public ResponseEntity<CodeSubmission> reviseCode(@RequestParam(name = "submissionId") String submissionId,
            @RequestParam(name = "revisedCode") String revisedCode) {
        try {
            logger.info("제출 ID {}의 코드 수정 시작", submissionId);
            CodeSubmission submission = codeSubmissionService.reviseCode(submissionId, revisedCode);
            logger.info("제출 ID {}의 코드 수정 완료", submissionId);
            return ResponseEntity.ok(submission);
        } catch (IOException | InterruptedException e) {
            logger.error("제출 ID {}의 코드 수정 중 오류 발생. 오류: {}", submissionId, e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // 사용자 제출물 조회 API
    @GetMapping("/submissions")
    public ResponseEntity<List<CodeSubmission>> getUserSubmissions(@RequestParam(name = "userId") Long userId) {
        logger.info("사용자 {}의 제출물 조회 시작", userId);
        List<CodeSubmission> submissions = codeSubmissionService.getUserSubmissions(userId);

        if (submissions.isEmpty()) {
            logger.warn("사용자 {}의 제출물이 없습니다", userId);
            return ResponseEntity.noContent().build();
        }

        logger.info("사용자 {}의 제출물 조회 완료. 제출물 개수: {}", userId, submissions.size());
        return ResponseEntity.ok(submissions);
    }

    // AI 피드백 제공 API
    @PostMapping("/feedback")
    public ResponseEntity<?> getAiFeedback(@RequestBody Map<String, Object> payload) {
        try {
            // AI 서버와 통신
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> aiResponse = restTemplate.postForEntity(AI_SERVER_URL, payload, Map.class);

            // AI 서버의 응답 반환
            return ResponseEntity.ok(aiResponse.getBody());
        } catch (Exception e) {
            // 오류 처리
            return ResponseEntity.status(500).body("AI 피드백 요청 처리 중 오류 발생: " + e.getMessage());
        }
    }
}
