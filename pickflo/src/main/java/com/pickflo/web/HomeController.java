package com.pickflo.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickflo.domain.User;
import com.pickflo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

	private final UserService userSvc;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@PostMapping("/mypage")
	public String mypage(@RequestParam("email") String email, @RequestParam("password") String password,
						RedirectAttributes redirectAttributes) {

		User user = userSvc.findByEmail(email);

		if (user != null) {
			if (passwordEncoder.matches(password, user.getPassword())) {
				return "mypage";
			} else {
				redirectAttributes.addFlashAttribute("errorMessage", "이메일 또는 비밀번호가 일치하지 않습니다.");
				return "redirect:/";
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "일치하는 회원이 없습니다.");
			return "redirect:/";
		}
	}

	@GetMapping("/user/signup")
	public void signup() {

	}

	@PostMapping("/user/signup")
	public String writeSignup(User User) {
		userSvc.create(User);
		return "home";
	}
}
