package com.photodiary.backend.diary.model;

import com.photodiary.backend.global.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(length=256)
    private String content;

    private boolean isPublic;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "diary_id")
    private List<Image> images = new ArrayList<>();
}
