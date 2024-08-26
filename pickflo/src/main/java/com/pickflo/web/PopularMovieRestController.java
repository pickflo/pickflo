package com.pickflo.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.PopularMovie;
import com.pickflo.service.PopularMovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 나중에 삭제할 파일

@Slf4j
@RequestMapping("/api/popular")
@RestController
@RequiredArgsConstructor
public class PopularMovieRestController {

	private final PopularMovieService movieService;

	@GetMapping("/save")
	public void savePopularMovies() {
		movieService.updatePopularMovies();
	}

	@GetMapping("/list")
	public ResponseEntity<List<PopularMovie>> readPopularMovies() {
		List<PopularMovie> list=movieService.readPopularMovies();
		log.info("리스트~~~~~~~~~~~{}",list);
		return ResponseEntity.ok(list);
	}

}
