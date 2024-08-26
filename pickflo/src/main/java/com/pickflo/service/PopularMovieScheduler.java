package com.pickflo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PopularMovieScheduler {

	private final PopularMovieService popuService;

	@Scheduled(cron = "0 44 14 * * *")  // 24시간마다 실행 (24 * 60 * 60 * 1000 ms)
	public void fetchAndSavePopularMovies() {
		popuService.updatePopularMovies();
	}
}
