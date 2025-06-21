package com.photodiary.backend.friend.service;

import com.photodiary.backend.friend.Exception.AlreadyRespondedException;
import com.photodiary.backend.friend.Exception.UnauthorizedFriendActionException;
import com.photodiary.backend.friend.dto.FriendRequestActionDto;
import com.photodiary.backend.friend.dto.FriendRequestResponseDto;
import com.photodiary.backend.friend.model.Friend;
import com.photodiary.backend.friend.model.FriendStatus;
import com.photodiary.backend.friend.repository.FriendRepository;
import com.photodiary.backend.friend.Exception.NoFriendRequestException;
import com.photodiary.backend.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                            .id(friend.getId())
                            .username(sender.getUsername())
                            .email(sender.getEmail())
                            .requestedAt(friend.getCreatedAt().toLocalDate())
                            .status(friend.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<FriendRequestResponseDto> getSentFriendRequests(Long userId) {

        //내가 보낸 친구 요청 목록 가져오기
        List<FriendStatus> statuses = List.of(FriendStatus.REQUESTED, FriendStatus.DECLINED);
        List<Friend> sentRequests = friendRepository.findAllByUserIdAndStatusIn(userId, statuses);
        if (sentRequests.isEmpty()) {
            throw new NoFriendRequestException("보낸 친구 요청이 없습니다.");
        }

        // 응답
        return sentRequests.stream()
                .map(friend -> {
                    User receiver = friend.getFriend();
                    return FriendRequestResponseDto.builder()
                            .id(friend.getId())
                            .username(receiver.getUsername())
                            .email(receiver.getEmail())
                            .requestedAt(friend.getCreatedAt().toLocalDate())
                            .status(friend.getStatus()) // 상태 포함
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void respondToFriendRequest(Long friendRequestId, Long userId, FriendRequestActionDto.FriendAction action) {

        Friend friendRequest = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new NoFriendRequestException("친구 요청이 존재하지 않습니다."));

        // 요청받은 사람인지 확인
        if (!friendRequest.getFriend().getId().equals(userId)) {
            throw new UnauthorizedFriendActionException("해당 요청에 대한 권한이 없습니다.");
        }

        if (friendRequest.getStatus() != FriendStatus.REQUESTED) {
            throw new AlreadyRespondedException("이미 처리된 요청입니다.");
        }

        if (action == FriendRequestActionDto.FriendAction.ACCEPT) {
            friendRequest.setStatus(FriendStatus.ACCEPTED);
        } else if (action == FriendRequestActionDto.FriendAction.DECLINE) {
            friendRequest.setStatus(FriendStatus.DECLINED);
        }

        // save는 JPA가 변경 감지로 생략 가능
    }

}

