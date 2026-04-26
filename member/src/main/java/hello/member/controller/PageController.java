package hello.member.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        Long memberId = (Long) session.getAttribute("loginMember");
        model.addAttribute("isLoggedIn", memberId != null);
        return "home";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}