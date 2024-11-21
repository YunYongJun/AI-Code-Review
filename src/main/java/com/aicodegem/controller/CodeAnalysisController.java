package com.aicodegem.controller;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.dto.RevisedCodeRequest;
import com.aicodegem.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    @PostMapping("/submit")
    public ResponseEntity<CodeSubmission> submitCode(@RequestBody CodeSubmissionRequest request) throws IOException {
        CodeSubmission submission = codeSubmissionService.submitCode(
                request.getUserId(),
                request.getCode(),
                request.getTitle());
        return ResponseEntity.ok(submission);
    }

    @PostMapping("/revise")
    public ResponseEntity<Map<String, String>> reviseCode(@RequestBody RevisedCodeRequest request) throws IOException {
        Map<String, String> result = codeSubmissionService.reviseCode(request.getSubmissionId(),
                request.getRevisedCode());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/improve/{submissionId}")
    public ResponseEntity<String> improveCode(@PathVariable String submissionId) {
        String improvedCode = codeSubmissionService.improveCode(submissionId);
        return ResponseEntity.ok(improvedCode);
    }
}
