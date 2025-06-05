package com.photodiary.backend.friend.model;

import com.photodiary.backend.global.common.model.BaseEntity;
import com.photodiary.backend.user.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Friend extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friendId", nullable = false)
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;
}
