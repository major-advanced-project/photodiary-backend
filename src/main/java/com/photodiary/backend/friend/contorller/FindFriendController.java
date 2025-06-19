package com.photodiary.backend.friend.contorller;

import com.photodiary.backend.friend.Exception.NoFriendFoundException;
import com.photodiary.backend.friend.dto.FindFriendResponseDto;
import com.photodiary.backend.friend.service.FindFriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("friends")
public class FindFriendController {
    private final FindFriendService findFriendService;

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
}





