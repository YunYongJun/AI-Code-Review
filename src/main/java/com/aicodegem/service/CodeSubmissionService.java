package com.aicodegem.service;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeSubmissionService {

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private AIAnalysisService aiAnalysisService;

    // 최초 코드 제출 처리
    public CodeSubmission submitCode(CodeSubmissionRequest request) {
        String code = request.getCode();
        int score = aiAnalysisService.analyzeCode(code);
        String feedback = aiAnalysisService.generateFeedback(code);

        // 제출된 코드와 결과를 저장
        CodeSubmission submission = new CodeSubmission(request.getUserId(), code, feedback, score);
        return codeRepository.save(submission);
    }

    // 수정된 코드 제출 처리
    public CodeSubmission resubmitCode(String userId, String revisedCode) {
        CodeSubmission submission = codeRepository.findByUserId(userId);
        int revisedScore = aiAnalysisService.analyzeCode(revisedCode);
        String revisedFeedback = aiAnalysisService.generateFeedback(revisedCode);

        // 기존 코드 제출에 수정된 결과 반영
        submission.setRevisedCode(revisedCode);
        submission.setRevisedScore(revisedScore);
        submission.setFeedback(revisedFeedback);

        return codeRepository.save(submission);
    }
}
