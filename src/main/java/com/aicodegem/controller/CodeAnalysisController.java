package com.aicodegem.controller;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    // 최초 코드 제출 API - USER 역할을 가진 로그인된 사용자만 접근 가능
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit")
    public ResponseEntity<CodeSubmission> submitCode(@RequestBody CodeSubmissionRequest request,
            Authentication authentication) {
        String userId = authentication.getName(); // 인증된 사용자의 ID를 가져옴
        request.setUserId(userId);
        CodeSubmission submission = codeSubmissionService.submitCode(request);
        return ResponseEntity.ok(submission);
    }

    // 수정된 코드 제출 API - USER 역할을 가진 로그인된 사용자만 접근 가능
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/resubmit")
    public ResponseEntity<CodeSubmission> resubmitCode(@RequestBody String revisedCode, Authentication authentication) {
        String userId = authentication.getName(); // 인증된 사용자 ID
        CodeSubmission submission = codeSubmissionService.resubmitCode(userId, revisedCode);
        return ResponseEntity.ok(submission);
    }

    // 특정 사용자의 모든 제출 코드 조회 API - USER 역할을 가진 로그인된 사용자만 접근 가능
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/submissions")
    public ResponseEntity<List<CodeSubmission>> getSubmissions(Authentication authentication) {
        String userId = authentication.getName(); // 인증된 사용자 ID
        List<CodeSubmission> submissions = codeSubmissionService.getAllSubmissionsByUserId(userId);
        return ResponseEntity.ok(submissions);
    }

    // 특정 제출 코드 조회 API - USER 역할을 가진 로그인된 사용자만 접근 가능
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<CodeSubmission> getSubmissionById(@PathVariable String submissionId) {
        CodeSubmission submission = codeSubmissionService.getSubmissionById(submissionId);
        return submission != null ? ResponseEntity.ok(submission) : ResponseEntity.notFound().build();
    }
}
