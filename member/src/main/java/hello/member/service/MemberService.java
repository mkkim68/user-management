package hello.member.service;

import hello.member.domain.Member;
import hello.member.dto.LoginRequest;
import hello.member.dto.SignupRequest;
import hello.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signup(SignupRequest request) {
        // 중복 체크
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setPassword(request.getPassword()); // 실무에선 암호화 필요
        member.setName(request.getName());

        return memberRepository.save(member);
    }

    public Member login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!member.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀립니다.");
        }

        return member;
    }
}