package com.pickflo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.Movie;
import com.pickflo.repository.MovieRepository;
import com.pickflo.repository.UserMoviePickRepository;
import com.pickflo.service.MovieService;
import com.pickflo.service.UserMoviePickService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
public class MovieLikeRestController {
	
	private final UserMoviePickRepository userMoviePickRepo;
	private final UserMoviePickService userMoviePickSvc;

	@GetMapping("/like-status")
	public Boolean getLikeStatus(@RequestParam Long userId, @RequestParam Long movieId) {
        boolean isFavorite = userMoviePickRepo.existsByUserIdAndMovieId(userId, movieId);
        return isFavorite;
    }
	
	@GetMapping("/like")
    public void likeMovie(@RequestParam Long userId, @RequestParam Long movieId) {
		userMoviePickSvc.addPick(userId, movieId);
    }
	
	@GetMapping("/unlike")
    public void unlikeMovie(@RequestParam Long userId, @RequestParam Long movieId) {
		userMoviePickSvc.removePick(userId, movieId);
    }
	
}
