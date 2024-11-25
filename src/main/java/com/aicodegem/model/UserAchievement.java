package com.aicodegem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_achievement")
public class UserAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_achievement_id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // User 테이블과 연결되는 외래키
    private User user; // User 엔티티와 연결

    @ManyToOne
    @JoinColumn(name = "achievement_id", nullable = false) // Achievement 테이블과 연결되는 외래키
    private Achievement achievement; // Achievement 엔티티와 연결

    @Column(name = "date_achieved", nullable = false)
    private LocalDate dateAchieved; // 업적 달성 날짜

    public UserAchievement(Long id, User user, Achievement achievement, LocalDate dateLocalDate) {
        this.Id = id == null ? 0L : id;
        this.user = user;
        this.achievement = achievement;
        this.dateAchieved = dateLocalDate;

    }

}
