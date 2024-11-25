package com.aicodegem.controller;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
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

    // private static final Logger logger =
    // LoggerFactory.getLogger(CodeAnalysisController.class); // Logger 생성

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    private static final String AI_SERVER_URL = "http://192.168.34.13:8888/predict"; // AI 서버 URL

    @PostMapping("/submit")
    public ResponseEntity<CodeSubmission> submitCode(@RequestParam Long userId, @RequestParam String code,
            @RequestParam String title) {
        try {
            CodeSubmission submission = codeSubmissionService.submitCode(userId, code, title);
            return ResponseEntity.ok(submission);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/revise")
    public ResponseEntity<CodeSubmission> reviseCode(@RequestParam String submissionId,
            @RequestParam String revisedCode) {
        try {
            CodeSubmission submission = codeSubmissionService.reviseCode(submissionId, revisedCode);
            return ResponseEntity.ok(submission);
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // 사용자 제출물 조회 API
    @GetMapping("/submissions")
    public ResponseEntity<List<CodeSubmission>> getUserSubmissions(@RequestParam Long userId) {
        List<CodeSubmission> submissions = codeSubmissionService.getUserSubmissions(userId);

        if (submissions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

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
