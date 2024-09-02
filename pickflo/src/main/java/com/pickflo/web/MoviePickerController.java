package com.pickflo.web;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pickflo.domain.User;
import com.pickflo.service.UserMoviePickService;
import com.pickflo.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/movie/picker")
public class MoviePickerController {

	@GetMapping("")
	public void showPickerPage(Model model, Authentication authentication) {

	}
}
