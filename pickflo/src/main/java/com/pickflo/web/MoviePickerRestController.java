package com.pickflo.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.Movie;
import com.pickflo.service.MoviePickerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/picker")
public class MoviePickerRestController {

	private final MoviePickerService svc;

	@GetMapping("/list")
	public ResponseEntity<List<Movie>> readMovies() {
		List<Movie> list=svc.readMovies();
		log.info("@@@@@@@@@@controller-list={}",list);
		return ResponseEntity.ok(list);
	}
}
