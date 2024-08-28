package com.pickflo.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pickflo.domain.User;
import com.pickflo.service.UserService;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {
	
	   private final UserService userSvc;

	   public HomeController(UserService userSvc) {
	        this.userSvc = userSvc;
	    }
	
    @GetMapping("/home")
    public String home(Model model) {
        // 현재 인증된 사용자 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        if (email != null) {
            User user = userSvc.findByEmail(email);
            model.addAttribute("user", user);
        } else {
            log.warn("사용자가 인증되지 않았습니다.");
            return "redirect:/user/signin"; // 인증되지 않은 경우 로그인 페이지로 리다이렉트
        }

        return "home"; // home.html 페이지로 이동
    }
    
    
}

