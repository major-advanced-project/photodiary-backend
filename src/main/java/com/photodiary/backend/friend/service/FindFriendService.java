package com.photodiary.backend.friend.service;

import com.photodiary.backend.diary.dto.FindDiaryResponseDto;
import com.photodiary.backend.diary.model.Diary;
import com.photodiary.backend.diary.repository.DiaryRepository;
import com.photodiary.backend.friend.dto.FindFriendByIdResponse;
import com.photodiary.backend.friend.dto.FriendRequestResponseDto;
import com.photodiary.backend.global.exception.EmptyDiaryList;
import com.photodiary.backend.friend.Exception.NoFriendFoundException;
import com.photodiary.backend.friend.Exception.NotPublicDiary;
import com.photodiary.backend.friend.dto.FindFriendResponseDto;
import com.photodiary.backend.friend.model.Friend;
import com.photodiary.backend.friend.repository.FriendRepository;
import com.photodiary.backend.global.exception.NotFriendRelation;
import com.photodiary.backend.global.exception.UserNotFoundException;
import com.photodiary.backend.user.model.User;
import com.photodiary.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FindFriendService{
    private final FriendRepository friendRepository;
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

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

    public FindDiaryResponseDto findFriendDiary(long diaryId) {
        Optional<Diary> opt = diaryRepository.findById(diaryId);
        if(opt.isEmpty()){
            throw new RuntimeException("일기가 존재하지 않습니다");
        }
        if(!opt.get().isPublic()){
            throw new NotPublicDiary("공개된 일기가 아닙니다.");
        }
        return FindDiaryResponseDto.entityToDto(opt.get());
    }


    public List<FindDiaryResponseDto> findFriendDairyList(long userId, long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        List<Diary> diaryList = diaryRepository.findAllByUserId(friendId);

        if(! (friendRepository.existsByUserAndFriend(user,friend)||friendRepository.existsByUserAndFriend(friend,user))){
            throw new NotFriendRelation("친구 관계가 아닙니다.");
        }

        if(diaryList.isEmpty()){
            throw new EmptyDiaryList("작성된 일기가 없습니다.");
        }

        return diaryList.stream()
                .filter(Diary::isPublic)        //기존 일기 조회에서 친구일기 조회일때 public을 확인하는 filter추가
                .map(FindDiaryResponseDto::entityToDto)
                .collect(Collectors.toList());
    }

    public FindFriendByIdResponse findFriendById(Long friendId) {
        Optional<User> user =  userRepository.findById(friendId);

        if(user.isEmpty()){
            throw new UserNotFoundException("해당 유저가 없습니다.");
        }


        return FindFriendByIdResponse.builder()
                .username(user.get().getUsername())
                .build();
    }
}
