package com.pickflo.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pickflo.domain.User;
import com.pickflo.dto.CustomUserDetails;
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
	
	@PreAuthorize("isAuthenticated()") //-> role에 상관없이 아이디/비밀번호로만 인증.
    @GetMapping("/")
    public String home() {
		
		// SecurityContext에서 현재 로그인한 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// 로그인한 사용자의 userId를 얻기
		Long userId = ((CustomUserDetails) userDetails).getId();
		
        // 현재 인증된 사용자정보 가져오기 -> url로 접속 막히위해
        String email = null;

        return "home";
    }
    
    
}

