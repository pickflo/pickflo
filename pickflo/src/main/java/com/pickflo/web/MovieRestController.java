package com.pickflo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/movie")
@RestController
@RequiredArgsConstructor
public class MovieRestController {
	
	private final MovieService movieSvc;
	
	@GetMapping("/save-movies")
	public void  getMovieId() {
		movieSvc.getMovieId();
	}
}
