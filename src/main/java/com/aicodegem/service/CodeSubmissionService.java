package com.aicodegem.service;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CodeSubmissionService {

    private static final Logger logger = LoggerFactory.getLogger(CodeSubmissionService.class);

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private AIAnalysisService aiAnalysisService;

    @Autowired
    private RankingService rankingService;

    // 최초 코드 제출 처리
    public CodeSubmission submitCode(CodeSubmissionRequest request) {
        String code = request.getCode();
        logger.info("사용자 ID {}로부터 최초 코드 제출을 받았습니다.", request.getUserId());

        int score = aiAnalysisService.analyzeCode(code);
        String feedback = aiAnalysisService.generateFeedback(code);
        logger.debug("코드 분석 결과 - 점수: {}, 피드백: {}", score, feedback);

        // 제출된 코드와 결과를 저장
        CodeSubmission submission = new CodeSubmission(request.getUserId(), code, feedback, score);

        // 처음 제출 시, ranking 테이블에 점수 추가
        rankingService.updateTotalScore(request.getUserId(), score, 0);
        logger.info("코드 제출을 저장하고 사용자 {}의 순위 점수를 업데이트했습니다.", request.getUserId());

        return codeRepository.save(submission);
    }

    // 수정된 코드 제출 처리
    public CodeSubmission resubmitCode(Long userId, String revisedCode) {
        logger.info("사용자 ID {}로부터 코드 재제출을 받았습니다.", userId);

        CodeSubmission submission = codeRepository.findByUserId(userId);
        int previousScore = submission.getRevisedScore() != 0 ? submission.getRevisedScore()
                : submission.getInitialScore();
        int revisedScore = aiAnalysisService.analyzeCode(revisedCode);
        String revisedFeedback = aiAnalysisService.generateFeedback(revisedCode);

        logger.debug("수정된 코드 분석 결과 - 점수: {}, 피드백: {}", revisedScore, revisedFeedback);

        // 기존 코드 제출에 수정된 결과 반영
        submission.setRevisedCode(revisedCode);
        submission.setRevisedScore(revisedScore);
        submission.setFeedback(revisedFeedback);

        // 수정된 점수로 ranking 테이블 업데이트 (이전 점수는 빼고 새 점수는 더함)
        rankingService.updateTotalScore(userId, revisedScore, previousScore);
        logger.info("사용자 {}의 코드 재제출 결과를 저장하고 순위 점수를 업데이트했습니다.", userId);

        return codeRepository.save(submission);
    }

    // 특정 사용자 ID의 모든 제출 기록 조회
    public List<CodeSubmission> getAllSubmissionsByUserId(Long userId) {
        logger.info("사용자 ID {}의 모든 제출 기록을 조회합니다.", userId);
        return codeRepository.findAllByUserId(userId);
    }

    // 특정 submissionId로 제출 코드 조회
    public CodeSubmission getSubmissionById(String submissionId) {
        logger.info("제출 ID {}로 제출 코드 조회 요청을 받았습니다.", submissionId);
        return codeRepository.findById(submissionId).orElse(null);
    }
}
