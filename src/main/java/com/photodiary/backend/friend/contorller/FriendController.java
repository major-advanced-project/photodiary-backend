package com.photodiary.backend.friend.contorller;

import com.photodiary.backend.diary.dto.FindDiaryResponseDto;
import com.photodiary.backend.friend.Exception.CannotAddYourselfAsFriendException;
import com.photodiary.backend.friend.Exception.FriendAlreadyExistsException;
import com.photodiary.backend.friend.Exception.NoFriendFoundException;
import com.photodiary.backend.friend.dto.AddFriendRequestDto;
import com.photodiary.backend.friend.dto.AddFriendResponseDto;
import com.photodiary.backend.friend.dto.FindFriendResponseDto;
import com.photodiary.backend.friend.service.AddFriendService;
import com.photodiary.backend.friend.service.FindFriendService;
import com.photodiary.backend.global.exception.EmptyDiaryList;
import com.photodiary.backend.global.exception.NotFriendRelation;
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

    //친구 목록조회
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

    // 친구 일기 상세 조회
    @GetMapping("/{diaryId}")
    public ResponseEntity<?> findDiary(@PathVariable long diaryId) {
        log.info("[findDiary] diaryId = {}", diaryId);

        try {
            FindDiaryResponseDto result = findFriendService.findFriendDiary(diaryId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    //친구 일기 목록 조회
    @GetMapping("/diarys")
    public ResponseEntity<?> findFriendDiaryList(@RequestParam long friendId) {
        log.info("[findFriendDiaryList]");

        long userId = 1L;

        try{
            List<FindDiaryResponseDto> diaryResponseDtoList = findFriendService.findFriendDairyList(userId,friendId);
            return ResponseEntity.ok(diaryResponseDtoList);
        } catch (EmptyDiaryList | NotFriendRelation e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

}





