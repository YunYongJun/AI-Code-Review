package com.aicodegem.service;

import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class PylintService {

    private final String PYTHON_SCRIPT_PATH = "./pylint_runner.py"; // pylint_runner.py 파일 경로

    public String runPylint(String code) throws IOException, InterruptedException {
        // 1. 임시 Python 파일 생성
        File tempFile = File.createTempFile("code", ".py");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(code);
        }

        // 2. Python 스크립트를 호출하여 Pylint 실행
        ProcessBuilder processBuilder = new ProcessBuilder("python", PYTHON_SCRIPT_PATH, tempFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        StringBuilder pylintOutput = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                pylintOutput.append(line).append("\n");
            }
        }

        // 3. 임시 파일 삭제
        tempFile.delete();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Pylint failed with exit code: " + exitCode);
        }

        return pylintOutput.toString();
    }
}
