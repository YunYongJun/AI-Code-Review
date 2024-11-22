package com.aicodegem.controller;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

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
}
