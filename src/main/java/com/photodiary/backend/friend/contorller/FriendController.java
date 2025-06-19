package com.photodiary.backend.friend.contorller;

import com.photodiary.backend.friend.Exception.CannotAddYourselfAsFriendException;
import com.photodiary.backend.friend.Exception.FriendAlreadyExistsException;
import com.photodiary.backend.friend.Exception.NoFriendFoundException;
import com.photodiary.backend.friend.dto.AddFriendRequestDto;
import com.photodiary.backend.friend.dto.AddFriendResponseDto;
import com.photodiary.backend.friend.dto.FindFriendResponseDto;
import com.photodiary.backend.friend.service.AddFriendService;
import com.photodiary.backend.friend.service.FindFriendService;
import com.photodiary.backend.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("friends")
public class FriendController {
    private final FindFriendService findFriendService;
    private final AddFriendService addFriendService;

    @GetMapping()
    public ResponseEntity<?> findFriendList() {
        log.info("[GET] /friends - 친구 목록 조회");
        long userId = 1L;

        try {
            List<FindFriendResponseDto> friendList = findFriendService.findFriendList(userId);
            return ResponseEntity.ok(friendList);
        } catch (NoFriendFoundException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    // 친구 추가
    @PostMapping
    public ResponseEntity<?> addFriend(@RequestBody AddFriendRequestDto dto) {
        long userId = 1L;
        log.info("[POST] /friends/{}/{} - 친구 추가 요청", userId, dto.getEmail());

        try {
            AddFriendResponseDto result = addFriendService.addFriend(userId, dto.getEmail());
            return ResponseEntity.ok(result);
        } catch (UserNotFoundException | FriendAlreadyExistsException | CannotAddYourselfAsFriendException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

}





