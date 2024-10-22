package com.aicodegem.controller;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
public class CodeAnalysisController {

    @Autowired
    private CodeSubmissionService codeSubmissionService;

    // 최초 코드 제출 API - 로그인된 사용자만 접근 가능
    @PostMapping("/submit")
    public CodeSubmission submitCode(@RequestBody CodeSubmissionRequest request, Authentication authentication) {
        String userId = authentication.getName(); // 인증된 사용자의 ID를 가져옴
        request.setUserId(userId);
        return codeSubmissionService.submitCode(request);
    }

    // 수정된 코드 제출 API
    @PostMapping("/resubmit")
    public CodeSubmission resubmitCode(@RequestBody String revisedCode, Authentication authentication) {
        String userId = authentication.getName(); // 인증된 사용자 ID
        return codeSubmissionService.resubmitCode(userId, revisedCode);
    }
}
