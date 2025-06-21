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

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "diaryId")
    @Builder.Default
    private List<Image> images = new ArrayList<>();

    public boolean isWriter(long userId) {
        return user.isSame(userId);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addImage(Image image){
        images.add(image);
    }
}
