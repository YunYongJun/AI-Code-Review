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

    @Autowired
    private RankingService rankingService;

    // 최초 코드 제출 처리
    public CodeSubmission submitCode(CodeSubmissionRequest request) {
        String code = request.getCode();
        int score = aiAnalysisService.analyzeCode(code);
        String feedback = aiAnalysisService.generateFeedback(code);

        System.out.println("코드: " + code);
        System.out.println("점수: " + score);
        System.out.println("피드백: " + feedback);

        // 제출된 코드와 결과를 저장
        CodeSubmission submission = new CodeSubmission(request.getUserId(), code, feedback, score);

        // 처음 제출 시, ranking 테이블에 점수 추가
        rankingService.updateTotalScore(request.getUserId(), score, 0);

        return codeRepository.save(submission);
    }

    // 수정된 코드 제출 처리
    public CodeSubmission resubmitCode(Long userId, String revisedCode) {
        CodeSubmission submission = codeRepository.findByUserId(userId);
        int previousScore = submission.getRevisedScore() != 0 ? submission.getRevisedScore()
                : submission.getInitialScore();
        int revisedScore = aiAnalysisService.analyzeCode(revisedCode);
        String revisedFeedback = aiAnalysisService.generateFeedback(revisedCode);

        // 기존 코드 제출에 수정된 결과 반영
        submission.setRevisedCode(revisedCode);
        submission.setRevisedScore(revisedScore);
        submission.setFeedback(revisedFeedback);

        // 수정된 점수로 ranking 테이블 업데이트 (이전 점수는 빼고 새 점수는 더함)
        rankingService.updateTotalScore(userId, revisedScore, previousScore);

        return codeRepository.save(submission);
    }
}
