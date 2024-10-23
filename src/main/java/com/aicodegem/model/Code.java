package com.aicodegem.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.aicodegem.Feedback;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "codes")
public class Code {
    @Id
    private String codeId;
    private String userId;
    private String submittedCode;
    private LocalDate submissionDate;
    private Feedback feedback;

    // 생성자, getter, setter 생략
}
