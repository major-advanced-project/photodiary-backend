package com.photodiary.backend.friend.service;

import com.photodiary.backend.friend.dto.FriendRequestResponseDto;
import com.photodiary.backend.friend.model.Friend;
import com.photodiary.backend.friend.model.FriendStatus;
import com.photodiary.backend.friend.repository.FriendRepository;
import com.photodiary.backend.global.exception.NoFriendRequestException;
import com.photodiary.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendRequestService {

    private final FriendRepository friendRepository;

    public List<FriendRequestResponseDto> getReceivedFriendRequests(Long userId) {
        List<Friend> requests = friendRepository.findAllByFriendIdAndStatus(userId, FriendStatus.REQUESTED);

        if (requests.isEmpty()) {
            throw new NoFriendRequestException("받은 친구 요청이 없습니다.");
        }
        

        return requests.stream()
                .map(friend -> {
                    User sender = friend.getUser(); // 요청 보낸 사람
                    return FriendRequestResponseDto.builder()
                            .username(sender.getUsername())
                            .email(sender.getEmail())
                            .requestedAt(friend.getCreatedAt().toLocalDate())
                            .build();
                })
                .collect(Collectors.toList());
    }
}

