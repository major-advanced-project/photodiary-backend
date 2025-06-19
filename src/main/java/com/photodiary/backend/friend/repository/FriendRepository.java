package com.photodiary.backend.friend.repository;

import com.photodiary.backend.friend.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    //친구A -> 친구B 인 상태랑 친구B -> 친구A 인 상태 모두 조회 (Accepted 상태만)
    @Query("""
    SELECT f FROM Friend f 
    WHERE (f.user.id = :userId OR f.friend.id = :userId)
    AND f.status = 'ACCEPTED'
""")
    List<Friend> findAllFriendsByUser(@Param("userId") long userId);


}
