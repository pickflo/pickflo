package com.pickflo.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickflo.domain.User;
import com.pickflo.dto.UserSignupDto;
import com.pickflo.service.UserMoviePickService;
import com.pickflo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SigninController {

	private final UserService userSvc;
	private final UserMoviePickService UserMovieSvc;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/user/signin")
	public void signin() {

	}

	@PostMapping("/user/signin")
	public String signin(@RequestParam("email") String email, @RequestParam("password") String password,
			RedirectAttributes redirectAttributes) {

		User user = userSvc.findByEmail(email);
		int pickedCount = UserMovieSvc.getPickedCountByUserId(user.getId());

		if (passwordEncoder.matches(password, user.getPassword())) {
			if (pickedCount < 3) {
				return "redirect:/movie/picker"; // 찜이 3개 미만이면 /movie/picker로 리다이렉트
			} else {
				return "redirect:/"; // 찜이 3개 이상이면 홈 페이지로 리다이렉트
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Invalid email or password.");
			return "redirect:/user/signin";
		}
	}

	@GetMapping("/user/signup")
	public void signup() {

	}

	@PostMapping("/user/signup")
	public String signup(UserSignupDto dto, RedirectAttributes redirectAttributes) {
		try {
			userSvc.create(dto);
			redirectAttributes.addFlashAttribute("signupSuccess");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("signupError");
		}
		return "redirect:/user/signin";
	}

}
