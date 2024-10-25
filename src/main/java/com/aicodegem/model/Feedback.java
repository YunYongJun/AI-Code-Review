package com.aicodegem.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "feedbacks") // "feedbacks" 컬렉션에 매핑
public class Feedback {
    @Id
    private String feedbackID; // 피드백의 고유 ID
    private String feedbackContent; // 피드백 내용
    private LocalDate feedbackDate; // 피드백 생성 날짜
}
