package com.pickflo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.Movie;
import com.pickflo.repository.MovieRepository;
import com.pickflo.repository.UserMoviePickRepository;
import com.pickflo.service.MovieLikeService;
import com.pickflo.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieLikeRestController {
	
	private final MovieLikeService moviceLikeSvc;

	@GetMapping("/like-status")
	public Boolean showLikeStatus(@RequestParam Long userId, @RequestParam Long movieId) {
        boolean isFavorite = moviceLikeSvc.getLikeStatus(userId, movieId);
        return isFavorite;
    }
	

}
