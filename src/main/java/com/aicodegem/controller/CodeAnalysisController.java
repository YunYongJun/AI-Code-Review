package com.aicodegem.controller;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    // 특정 사용자의 모든 제출 코드 조회 API
    @GetMapping("/submissions")
    public List<CodeSubmission> getSubmissions(@RequestParam Long userId) {
        return codeSubmissionService.getAllSubmissionsByUserId(userId);
    }

    // 특정 제출 코드 조회 API
    @GetMapping("/submissions/{submissionId}")
    public CodeSubmission getSubmissionById(@PathVariable String submissionId) {
        return codeSubmissionService.getSubmissionById(submissionId);
    }

// 코드 제출 API - AI 분석 후 저장
@PostMapping("/submit")
public ResponseEntity<CodeSubmission> submitCode(@RequestBody CodeSubmissionRequest request) {
    try {
        CodeSubmission submission = codeSubmissionService.submitCode(
            request.getUserId(),
            request.getCode(),
            request.getTitle() // title 필드 처리
        );
        return ResponseEntity.ok(submission);
    } catch (IOException e) {
        throw new RuntimeException("코드 제출 중 오류 발생: " + e.getMessage());
    }
}
}

