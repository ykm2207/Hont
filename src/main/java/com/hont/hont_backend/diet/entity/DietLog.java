package com.hont.hont_backend.diet.entity;

import com.hont.hont_backend.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diet_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class DietLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 식단 날짜
    @Column(nullable = false)
    private LocalDate dietDate;

    // 해당 날짜 식단 항목들
    @OneToMany(mappedBy = "dietLog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DietLogItem> items = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 총 칼로리 합산
    public double getTotalCalories() {
        return items.stream().mapToDouble(DietLogItem::getCalories).sum();
    }
}
