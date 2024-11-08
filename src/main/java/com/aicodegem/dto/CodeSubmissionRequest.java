package com.aicodegem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CodeSubmissionRequest {
    private Long userId;
    private String code;
}
