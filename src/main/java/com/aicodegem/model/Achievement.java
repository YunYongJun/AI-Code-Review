package com.aicodegem.model;

import jakarta.persistence.*; // JPA 관련 어노테이션을 위한 import
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "achievement") // 테이블 이름 지정
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략
    private Long id;

    @Column(nullable = false) // null을 허용하지 않음
    private Long userId; // 사용자 ID

    @Column(nullable = false) // null을 허용하지 않음
    private String achievementName; // 업적 이름

    private String achievementDesc; // 업적 설명 (null 허용)

    private LocalDate dateAchieved; // 달성 날짜

    // Getter와 Setter 생략
}
