package com.pickflo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/movie")
@Controller
public class MovieLikeController {
	
	@GetMapping("/like")
	public void like() {
		
	}
}
