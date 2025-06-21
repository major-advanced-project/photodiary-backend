package com.photodiary.backend.friend.service;

import com.photodiary.backend.friend.Exception.CannotAddYourselfAsFriendException;
import com.photodiary.backend.friend.Exception.FriendAlreadyExistsException;
import com.photodiary.backend.friend.dto.AddFriendResponseDto;
import com.photodiary.backend.friend.model.Friend;
import com.photodiary.backend.friend.model.FriendStatus;
import com.photodiary.backend.friend.repository.FriendRepository;
import com.photodiary.backend.global.exception.UserNotFoundException;
import com.photodiary.backend.user.model.User;
import com.photodiary.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddFriendService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional
    public AddFriendResponseDto addFriend(Long userId, String email) {
        User user = null;
        User friend = null;
        try {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
            friend = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        boolean exists = friendRepository.existsByUserAndFriend(user, friend) ||
                friendRepository.existsByUserAndFriend(friend, user);

        if(user.equals(friend)) {
            throw new CannotAddYourselfAsFriendException("나 자신을 친구로 추가할 수 없습니다.");
        }

        if (exists) {
            throw new FriendAlreadyExistsException("이미 등록된 친구입니다.");
        }

        Friend friendRelation = Friend.builder()
                .user(user)
                .friend(friend)
                .status(FriendStatus.ACCEPTED)
                .build();

        friendRepository.save(friendRelation);

        return AddFriendResponseDto.from(userId, friend.getId());
    }


}
