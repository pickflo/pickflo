package com.pickflo.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.PopularMovie;
import com.pickflo.service.PopularMovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/popular")
@RestController
@RequiredArgsConstructor
public class PopularMovieRestController {

	private final PopularMovieService movieSvc;

	@GetMapping("/list")
	public ResponseEntity<List<PopularMovie>> readPopularMovies() {
		List<PopularMovie> list=movieSvc.readPopularMovies();
		return ResponseEntity.ok(list);
	}

	// 나중에 삭제할 코드
	@GetMapping("/save")
	public void savePopularMovies() {
		movieSvc.updatePopularMovies();
	}
}
