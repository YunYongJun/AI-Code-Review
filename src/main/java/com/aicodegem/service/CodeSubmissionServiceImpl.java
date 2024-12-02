package com.aicodegem.service;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CodeSubmissionServiceImpl implements CodeSubmissionService {

    // private static final Logger logger =
    // LoggerFactory.getLogger(CodeSubmissionService.class);

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private PylintService pylintService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private AchievementService achievementService;

    public CodeSubmission submitCode(Long userId, String code, String title) throws IOException, InterruptedException {
        PylintService.PylintResult pylintResult = pylintService.runPylint(code);

        // pylint 점수로 초기 점수 설정
        int initialScore = (int) pylintResult.getScore();

        CodeSubmission submission = new CodeSubmission(userId, code, title);
        submission.setPylintOutput(pylintResult.getOutput());
        submission.setInitialScore(initialScore);
        submission.setSubmissionDate(LocalDate.now());

        // 랭킹 점수 업데이트 (pylint 점수를 사용)
        rankingService.updateTotalScore(userId, initialScore);

        // 업적 할당
        achievementService.assignAchievementsByTotalScore(userId);

        return codeRepository.save(submission);
    }

    public CodeSubmission reviseCode(String submissionId, String revisedCode) throws IOException, InterruptedException {
        Optional<CodeSubmission> optionalSubmission = codeRepository.findById(submissionId);

        if (optionalSubmission.isEmpty()) {
            throw new RuntimeException("해당 제출물이 존재하지 않습니다.");
        }

        CodeSubmission submission = optionalSubmission.get();
        PylintService.PylintResult pylintResult = pylintService.runPylint(revisedCode);

        int revisedScore = (int) pylintResult.getScore();

        // 제출된 데이터 적재
        submission.setRevisedCode(revisedCode);
        submission.setRevisedPylintOutput(pylintResult.getOutput());
        submission.setRevisedScore(revisedScore);
        submission.setFeedbackDate(LocalDate.now());

        // 수정된 점수로 랭킹 점수 업데이트
        rankingService.updateTotalScore(submission.getUserId(), revisedScore);

        // 업적 할당
        achievementService.assignAchievementsByTotalScore(submission.getUserId());

        return codeRepository.save(submission);
    }

    // 사용자 제출물 조회
    public List<CodeSubmission> getUserSubmissions(Long userId) {
        return codeRepository.findAllByUserId(userId);
    }
}