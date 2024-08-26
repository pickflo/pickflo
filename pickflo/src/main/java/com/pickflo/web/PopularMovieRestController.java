package com.pickflo.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.service.PopularMovieService;

import lombok.RequiredArgsConstructor;

// 나중에 삭제할 파일

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class PopularMovieRestController {

	private final PopularMovieService movieService;


    @GetMapping("/popular/save")
    public void savePopularMovies() {
        movieService.updatePopularMovies();
    }
}
