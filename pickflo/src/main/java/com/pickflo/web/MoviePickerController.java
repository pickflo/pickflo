package com.pickflo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/movie/picker")
public class MoviePickerController {

	@GetMapping("")
	public void picker(HttpSession session,Model model) {
		Long user=(Long) session.getAttribute("user");
		log.info("user============={}",user);
		
	}
	
}
