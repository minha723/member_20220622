package com.its.member.repository;


import com.its.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

//    @Query(value = "from MemberEntity where memberEmail=memberEmail")
//    MemberEntity login(String memberEmail);

    Optional<MemberEntity> findByMemberEmail(String memberEmail);
}
