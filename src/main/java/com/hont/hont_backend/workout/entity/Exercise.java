package com.hont.hont_backend.workout.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 운동 이름 (예: 스쿼트, 벤치프레스)
    @Column(nullable = false)
    private String name;

    // 운동 부위 (예: 하체, 가슴, 등)
    @Column(nullable = false)
    private String bodyPart;

    // 운동 설명
    private String description;

    // 운동 이미지 URL
    private String imageUrl;
}
