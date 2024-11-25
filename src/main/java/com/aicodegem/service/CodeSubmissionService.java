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
public class CodeSubmissionService {

    @Autowired
    private CodeRepository codeRepository;

    @Autowired
    private PylintService pylintService;

    public CodeSubmission submitCode(Long userId, String code, String title) throws IOException, InterruptedException {
        PylintService.PylintResult pylintResult = pylintService.runPylint(code);

        CodeSubmission submission = new CodeSubmission(userId, code, title);
        submission.setPylintOutput(pylintResult.getOutput());
        submission.setInitialScore((int) pylintResult.getScore());
        submission.setSubmissionDate(LocalDate.now());

        return codeRepository.save(submission);
    }

    public CodeSubmission reviseCode(String submissionId, String revisedCode) throws IOException, InterruptedException {
        Optional<CodeSubmission> optionalSubmission = codeRepository.findById(submissionId);

        if (optionalSubmission.isEmpty()) {
            throw new RuntimeException("해당 제출물이 존재하지 않습니다.");
        }

        CodeSubmission submission = optionalSubmission.get();
        PylintService.PylintResult pylintResult = pylintService.runPylint(revisedCode);

        submission.setRevisedCode(revisedCode);
        submission.setRevisedPylintOutput(pylintResult.getOutput());
        submission.setRevisedScore((int) pylintResult.getScore());
        submission.setFeedbackDate(LocalDate.now());

        System.out.println(submission.getInitialScore() + " " + submission.getRevisedScore());
        return codeRepository.save(submission);
    }

    // 사용자 제출물 조회
    public List<CodeSubmission> getUserSubmissions(Long userId) {
        return codeRepository.findAllByUserId(userId);
    }
}
