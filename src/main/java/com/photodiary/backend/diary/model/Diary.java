package com.photodiary.backend.diary.model;

import com.photodiary.backend.global.common.model.BaseEntity;
import com.photodiary.backend.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(length=256)
    private String content;

    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "diaryId")
    private List<Image> images = new ArrayList<>();
}
