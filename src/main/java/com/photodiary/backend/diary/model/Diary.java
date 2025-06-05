package com.photodiary.backend.diary.model;

import com.photodiary.backend.global.common.model.BaseEntity;
import com.photodiary.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Diary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

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
