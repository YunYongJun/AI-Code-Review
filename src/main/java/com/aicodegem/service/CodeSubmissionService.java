package com.aicodegem.service;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CodeSubmissionService {

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private AIAnalysisService aiAnalysisService;

    // 최초 코드 제출 처리
    public CodeSubmission submitCode(String userId, String code) throws IOException {
        int score = aiAnalysisService.analyzeCode(code); // AI 모델에서 점수 분석
        String feedback = aiAnalysisService.generateFeedback(code); // AI 모델에서 피드백 생성

        // 제출된 코드와 결과를 저장
        CodeSubmission submission = new CodeSubmission(Long.parseLong(userId), code, feedback, score);
        return codeRepository.save(submission);
    }

    // 수정된 코드 제출 처리
    public CodeSubmission resubmitCode(Long userId, String revisedCode) throws IOException {
        // 기존 코드 제출 내역 가져오기
        CodeSubmission submission = codeRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 ID에 대한 제출 코드가 없습니다."));

        // 수정된 코드에 대한 점수 및 피드백 분석
        int revisedScore = aiAnalysisService.analyzeCode(revisedCode);
        String revisedFeedback = aiAnalysisService.generateFeedback(revisedCode);

        // 수정된 결과 반영
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
