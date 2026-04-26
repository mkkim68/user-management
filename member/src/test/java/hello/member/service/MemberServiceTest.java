package hello.member.service;

import hello.member.domain.Member;
import hello.member.dto.LoginRequest;
import hello.member.dto.SignupRequest;
import hello.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setPassword("1234");
        request.setName("테스트");

        Member member = memberService.signup(request);

        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getName()).isEqualTo("테스트");
    }

    @Test
    @DisplayName("중복 이메일 가입 실패")
    void signupDuplicateEmail() {
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@test.com");
        request1.setPassword("1234");
        request1.setName("테스트1");
        memberService.signup(request1);

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@test.com");
        request2.setPassword("5678");
        request2.setName("테스트2");

        assertThatThrownBy(() -> memberService.signup(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setPassword("1234");
        signupRequest.setName("테스트");
        memberService.signup(signupRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("1234");

        Member member = memberService.login(loginRequest);

        assertThat(member.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("존재하지 않는 이메일 로그인 실패")
    void loginFailWrongEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@test.com");
        loginRequest.setPassword("1234");

        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 이메일입니다.");
    }

    @Test
    @DisplayName("비밀번호 틀림 로그인 실패")
    void loginFailWrongPassword() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setPassword("1234");
        signupRequest.setName("테스트");
        memberService.signup(signupRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("wrong");

        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 틀립니다.");
    }
}