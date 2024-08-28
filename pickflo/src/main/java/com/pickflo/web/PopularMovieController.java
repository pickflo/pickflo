package com.pickflo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/movie/popular")
public class PopularMovieController {

	@GetMapping("")
	public void popular() {
	}
	
}
