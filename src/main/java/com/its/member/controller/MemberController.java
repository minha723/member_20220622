package com.its.member.controller;

import com.its.member.dto.MemberDTO;
import com.its.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/save-form")
    public String saveForm() {
        return "memberPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "memberPages/login";
    }

    @GetMapping("/login-form")
    public String loginForm(@RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL,
                            Model model) {
        model.addAttribute("redirectURL", redirectURL);
        return "memberPages/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                        @RequestParam(value = "redirectURL", defaultValue = "/") String redirectURL) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginId", loginResult.getId());
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
//            return "memberPages/main";
            return "redirect:" + redirectURL; //로그인 하지 않은 사용자가 직전에 로그인 직전에 요청한 주소로 보내줌
        } else {
            return "memberPages/login";
        }
    }

    @GetMapping("/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/list";
    }

// /member/3
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model){
        MemberDTO findMemberDTO = memberService.findById(id);
        model.addAttribute("member", findMemberDTO);
        return "memberPages/detail";
    }

    @PostMapping("/ajax/{id}")
    public @ResponseBody MemberDTO findByIdAjax(@PathVariable Long id){
        MemberDTO findMemberDTO = memberService.findById(id);
        return findMemberDTO;
    }


    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model){
        MemberDTO updateMemberDTO = memberService.findById(id);
        model.addAttribute("updateMember", updateMemberDTO);
        return "memberPages/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        Long updateId = memberService.update(memberDTO);
        return "redirect:/member/"+ updateId;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateAjax(@RequestBody MemberDTO memberDTO){
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id){
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    /**
     * /member/3:조회(get) R, 저장(post) C, 수정(put) U, 삭제(delete) D
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByIdAjax(@PathVariable Long id){
        memberService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK); //ajax 호출한 부분에 리턴으로 200 응답을 줌
    }

    @PostMapping("/dup-check")
    public @ResponseBody String duplicateCheck(@RequestParam String memberEmail){
        String result = memberService.duplicateCheck(memberEmail);
        return  result;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate(); //세선 전체를 무효화
        session.removeAttribute("loginEmail"); //loginEmail만 세션값 삭제
        return "redirect:/";
    }
}
