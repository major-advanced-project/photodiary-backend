package com.photodiary.backend.friend.service;

import com.photodiary.backend.friend.Exception.NoFriendFoundException;
import com.photodiary.backend.friend.dto.FindFriendResponseDto;
import com.photodiary.backend.friend.model.Friend;
import com.photodiary.backend.friend.repository.FriendRepository;
import com.photodiary.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindFriendService{
    private final FriendRepository friendRepository;

    public List<FindFriendResponseDto> findFriendList(long userId) {
        List<Friend> friendList = friendRepository.findAllFriendsByUser(userId);

        if (friendList.isEmpty()) {
            throw new NoFriendFoundException("친구가 없습니다.");
        }

        return friendList.stream()
                .map(friend -> {
                    // 내가 친구 요청 보낸 쪽인지 받은 쪽인지 확인
                    User target = friend.getUser().getId() == userId
                            ? friend.getFriend()
                            : friend.getUser();
                    return FindFriendResponseDto.from(target);
                })
                .collect(Collectors.toList());
    }
}
