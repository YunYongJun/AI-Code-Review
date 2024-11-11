package com.aicodegem.service;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;

import java.util.List;
import java.util.Optional;

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
    public CodeSubmission resubmitCode(Long userId, String revisedCode) {
        CodeSubmission submission = codeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 ID에 대한 제출 코드가 없습니다."));

        int revisedScore = aiAnalysisService.analyzeCode(revisedCode);
        String revisedFeedback = aiAnalysisService.generateFeedback(revisedCode);

        // 기존 코드 제출에 수정된 결과 반영
        submission.setRevisedCode(revisedCode);
        submission.setRevisedScore(revisedScore);
        submission.setFeedback(revisedFeedback);

        return codeRepository.save(submission);
    }

    // 특정 사용자 ID의 모든 제출 기록 조회
    public List<CodeSubmission> getAllSubmissionsByUserId(Long userId) {
        return codeRepository.findAllByUserId(userId);
    }

    // 특정 submissionId로 제출 코드 조회
    public CodeSubmission getSubmissionById(String submissionId) {
        return codeRepository.findById(submissionId).orElse(null);
    }
}
