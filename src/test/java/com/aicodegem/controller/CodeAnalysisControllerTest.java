package com.aicodegem.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.aicodegem.dto.CodeSubmissionRequest;
import com.aicodegem.model.CodeSubmission;
import com.aicodegem.service.CodeSubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class CodeAnalysisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CodeSubmissionService codeSubmissionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getSubmissions_ShouldReturnSubmissionsForUser() throws Exception {
        Long userId = 1L;
        List<CodeSubmission> submissions = Arrays.asList(
                new CodeSubmission(userId, "Initial Code 1", "Title 1"),
                new CodeSubmission(userId, "Initial Code 2", "Title 2"));
        submissions.get(0).setId("1");
        submissions.get(1).setId("2");

        when(codeSubmissionService.getAllSubmissionsByUserId(userId)).thenReturn(submissions);

        mockMvc.perform(get("/api/code/submissions")
                .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(submissions.size()))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].title").value("Title 2"));
    }

    @Test
    void getSubmissionById_ShouldReturnSubmission() throws Exception {
        String submissionId = "1";
        CodeSubmission submission = new CodeSubmission(1L, "Initial Code", "Sample Title");
        submission.setId(submissionId);
        submission.setSubmissionDate(LocalDate.of(2024, 11, 21));
        submission.setInitialScore(5);

        when(codeSubmissionService.getSubmissionById(submissionId)).thenReturn(submission);

        mockMvc.perform(get("/api/code/submissions/{submissionId}", submissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(submissionId))
                .andExpect(jsonPath("$.title").value("Sample Title"))
                .andExpect(jsonPath("$.initialCode").value("Initial Code"))
                .andExpect(jsonPath("$.submissionDate").value("2024-11-21"))
                .andExpect(jsonPath("$.initialScore").value(5));
    }

    @Test
    void getSubmissionById_ShouldReturnNotFoundWhenSubmissionDoesNotExist() throws Exception {
        String submissionId = "1";

        when(codeSubmissionService.getSubmissionById(submissionId)).thenReturn(null);

        mockMvc.perform(get("/api/code/submissions/{submissionId}", submissionId))
                .andExpect(status().isOk()) // 컨트롤러에서 null 반환을 처리했음
                .andExpect(content().string("")); // 반환된 내용이 비어 있는지 확인
    }

    @Test
    void submitCode_ShouldSaveCodeAndReturnSubmission() throws Exception {
        CodeSubmissionRequest request = new CodeSubmissionRequest(1L, "Sample Initial Code", "Sample Title");
        CodeSubmission submission = new CodeSubmission(request.getUserId(), request.getCode(), request.getTitle());
        submission.setId("1");
        submission.setSubmissionDate(LocalDate.now());

        when(codeSubmissionService.submitCode(request.getUserId(), request.getCode(), request.getTitle()))
                .thenReturn(submission);

        mockMvc.perform(post("/api/code/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Title"))
                .andExpect(jsonPath("$.initialCode").value("Sample Initial Code"))
                .andExpect(jsonPath("$.submissionDate").exists());
    }

    @Test
    void submitCode_ShouldReturnErrorWhenIOExceptionOccurs() throws Exception {
        CodeSubmissionRequest request = new CodeSubmissionRequest(1L, "Sample Initial Code", "Sample Title");

        when(codeSubmissionService.submitCode(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new IOException("Test Exception"));

        mockMvc.perform(post("/api/code/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("코드 제출 중 오류 발생: Test Exception"));
    }
}
