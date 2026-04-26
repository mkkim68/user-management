package hello.member.controller;

import hello.member.domain.Member;
import hello.member.dto.LoginRequest;
import hello.member.dto.SignupRequest;
import hello.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {

        memberService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        Member member = memberService.login(request);
        session.setAttribute("loginMember", member.getId());
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Long memberId = (Long) session.getAttribute("loginMember");
        if (memberId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        return ResponseEntity.ok("로그인된 회원 ID: " + memberId);
    }
}