package com.photodiary.backend.friend.contorller;

import com.photodiary.backend.diary.dto.FindDiaryResponseDto;
import com.photodiary.backend.friend.Exception.CannotAddYourselfAsFriendException;
import com.photodiary.backend.friend.Exception.FriendAlreadyExistsException;
import com.photodiary.backend.friend.Exception.NoFriendFoundException;
import com.photodiary.backend.friend.dto.*;
import com.photodiary.backend.friend.service.AddFriendService;
import com.photodiary.backend.friend.service.FindFriendService;
import com.photodiary.backend.friend.service.FriendRequestService;
import com.photodiary.backend.global.exception.CustomException;
import com.photodiary.backend.global.exception.EmptyDiaryList;
import com.photodiary.backend.global.exception.NotFriendRelation;
import com.photodiary.backend.global.exception.UserNotFoundException;
import com.photodiary.backend.global.jwt.annotation.LoginUserId;
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
    private final FriendRequestService friendRequestService;

    //친구 목록조회
    @GetMapping()
    public ResponseEntity<?> findFriendList(@LoginUserId Long userId) {
        log.info("[GET] /friends - 친구 목록 조회");

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
    public ResponseEntity<?> addFriend(@RequestBody AddFriendRequestDto dto,@LoginUserId Long userId) {
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
    @GetMapping("diarys/{diaryId}")
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
    public ResponseEntity<?> findFriendDiaryList(@RequestParam long friendId,@LoginUserId Long userId) {
        log.info("[findFriendDiaryList]");


        try{
            List<FindDiaryResponseDto> diaryResponseDtoList = findFriendService.findFriendDairyList(userId,friendId);
            return ResponseEntity.ok(diaryResponseDtoList);
        } catch (EmptyDiaryList | NotFriendRelation e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    // 받은 친구 요청 목록 조회
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedFriendRequests(
            @LoginUserId Long userId) {

        try{
            List<FriendRequestResponseDto> requests = friendRequestService.getReceivedFriendRequests(userId);
            return ResponseEntity.ok(requests);
        }catch (CustomException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    @GetMapping("/sent")
    public ResponseEntity<?> getSentFriendRequests(@LoginUserId Long userId) {
        try{
            List<FriendRequestResponseDto> requests = friendRequestService.getSentFriendRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }

    @PostMapping("/response/{friendRequestId}")
    public ResponseEntity<?> respondToFriendRequest(
            @PathVariable Long friendRequestId,
            @RequestBody FriendRequestActionDto requestDto,
            @LoginUserId Long userId) {

        try{
            friendRequestService.respondToFriendRequest(friendRequestId, userId, requestDto.action());

            String message = switch (requestDto.action()) {
                case ACCEPT -> "친구 요청을 수락했습니다.";
                case DECLINE -> "친구 요청을 거절했습니다.";
            };

            return ResponseEntity.ok(Map.of("message", message));
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }

    }

    @GetMapping("/{friendId}")
    public ResponseEntity<?> findFriendById(@PathVariable Long friendId) {
        log.info("[findFriendById] friendId = {}", friendId);

        try{
            FindFriendByIdResponse requests = findFriendService.findFriendById(friendId);
            return ResponseEntity.ok(requests);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage())
            );
        }
    }


}





