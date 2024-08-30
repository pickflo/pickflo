package com.pickflo.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pickflo.domain.User;
import com.pickflo.dto.UserSignupDto;
import com.pickflo.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class SigninController {

	private final UserService userSvc;
	private final PasswordEncoder passwordEncoder;

	@GetMapping("/user/signin")
	public void signin() {

	}

	@PostMapping("/user/signin")
	public String signin(@RequestParam("email") String email, @RequestParam("password") String password,
			RedirectAttributes redirectAttributes,  HttpSession session) {

		User user = userSvc.findByEmail(email);

		if (user != null) {
			if (passwordEncoder.matches(password, user.getPassword())) {
				return "redirect:/home";
			} else {
				redirectAttributes.addFlashAttribute("errorMessage");
				return "redirect:/user/signin";
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage");
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
		 return "redirect:/home";
	    }


}
