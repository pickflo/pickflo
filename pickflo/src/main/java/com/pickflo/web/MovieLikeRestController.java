package com.pickflo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.service.MovieLikeService;

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

	private final MovieLikeService moviceLikeSvc;
	private final UserMoviePickService userMoviePickSvc;

	@GetMapping("/like-status")
	public Boolean showLikeStatus(@RequestParam Long userId, @RequestParam Long movieId) {
		boolean isFavorite = moviceLikeSvc.getLikeStatus(userId, movieId);
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
