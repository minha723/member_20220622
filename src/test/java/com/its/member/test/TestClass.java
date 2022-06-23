package com.its.member.test;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class TestClass {
    @Autowired
    private MemberService memberService;

    public MemberDTO newMember(int i) {
        MemberDTO member =
                new MemberDTO("테스트용이메일" + i, "테스트용비밀번호" + i, "테스트용이름" + i, 1 + i, "테스트용전화번호" + i);
        return member;
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원가입 테스트")
    public void saveTest() {
        MemberDTO memberDTO = new MemberDTO("email", "11", "민", 12, "010123");
        Long saveId = memberService.save(memberDTO);
        //Long saveId = memberService.save(newMember(1));
        MemberDTO findDTO = memberService.findById(saveId);
        assertThat(findDTO.getMemberEmail()).isEqualTo(memberDTO.getMemberEmail());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("목록 테스트")
    public void findAllTest() {

        IntStream.rangeClosed(1, 3).forEach(i -> {
            memberService.save(new MemberDTO("email" + i, "11", "민", 12, "010123"));
        });
        List<MemberDTO> findList = memberService.findAll();
        assertThat(findList.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인테스트")
    public void loginTest() {
        MemberDTO memberDTO = new MemberDTO("email", "11", "민", 12, "010123");
        Long saveId = memberService.save(memberDTO);
        //로그인 객체 생성 후 로그인
        MemberDTO loginMember = new MemberDTO();
        loginMember.setMemberEmail("email");
        loginMember.setMemberPassword("11");
        MemberDTO findMemberDTO = memberService.login(loginMember);
        //로그인 결과가 not null 이면 테스트 통과
        assertThat(findMemberDTO).isNotEqualTo(null);
    }

    @Test
    @DisplayName("회원 데이터 저장")
    public void memberSave() {
        IntStream.rangeClosed(1, 20).forEach(i -> {
            memberService.save(newMember(i));
        });
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("삭제 테이트")
    public void deleteTest(){
        Long saveId = memberService.save(newMember(80));
        memberService.deleteById(saveId);
        assertThat(memberService.findById(saveId)).isNull();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("수정 테이트")
    public void updateTest(){
        Long saveId = memberService.save(newMember(80));
        MemberDTO memberDTO = memberService.findById(saveId);
        MemberDTO updateMemberDTO = memberService.findById(saveId);
        updateMemberDTO.setMemberPhone("수정함");
        memberService.update(updateMemberDTO);
        assertThat(memberService.findById(saveId).getMemberPhone()).isNotEqualTo(memberDTO.getMemberPhone());
    }
}
