package com.aicodegem.service;

import com.aicodegem.model.CodeSubmission;
import com.aicodegem.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CodeSubmissionService {

    @Autowired
    private CodeRepository codeRepository;

    private String runPylint(String code) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("pylint", "-");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (var writer = process.getOutputStream()) {
            writer.write(code.getBytes());
            writer.flush();
        }

        StringBuilder pylintOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                pylintOutput.append(line).append("\n");
            }
        }

        return pylintOutput.toString();
    }

    public CodeSubmission submitCode(Long userId, String code, String title) throws IOException {
        String pylintResult = runPylint(code);

        CodeSubmission submission = new CodeSubmission(userId, code, title);
        submission.setPylintOutput(pylintResult);
        submission.setSubmissionDate(LocalDate.now());

        return codeRepository.save(submission);
    }

    public Map<String, String> reviseCode(String submissionId, String revisedCode) throws IOException {
        Optional<CodeSubmission> optionalSubmission = codeRepository.findById(submissionId);

        if (optionalSubmission.isEmpty()) {
            throw new RuntimeException("해당 제출물이 존재하지 않습니다.");
        }

        CodeSubmission submission = optionalSubmission.get();
        String pylintResult = runPylint(revisedCode);

        submission.setRevisedCode(revisedCode);
        submission.setRevisedPylintOutput(pylintResult);
        submission.setFeedbackDate(LocalDate.now());

        codeRepository.save(submission);

        Map<String, String> result = new HashMap<>();
        result.put("pylintOutput", pylintResult);
        result.put("submissionId", submission.getId());
        return result;
    }

    public String improveCode(String submissionId) {
        Optional<CodeSubmission> optionalSubmission = codeRepository.findById(submissionId);

        if (optionalSubmission.isEmpty()) {
            throw new RuntimeException("해당 제출물이 존재하지 않습니다.");
        }

        // Mocked AI improvement logic
        return """
                # Improved Code by AI
                def main():
                    print("Hello, World!")
                if __name__ == "__main__":
                    main()
                """;
    }
}
