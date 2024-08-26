package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickflo.domain.PopularMovie;
import com.pickflo.repository.PopularMovieRepository;
import com.pickflo.repository.TmdbClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PopularMovieService {

	private final TmdbClient tmdbClient;
	private final PopularMovieRepository popuRepo;

	@Value("${tmdb.api.key}")
	private String apiKey;

	@Value("${tmdb.language}")
	private String language;

	@Value("${tmdb.image.base.url}")
	private String imageBaseUrl; // 이미지 기본 URL

	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	public void updatePopularMovies() {
		// 기존 데이터를 삭제
		popuRepo.deleteAll();

		List<PopularMovie> movies = tmdbClient.getPopularMovies(apiKey, language).getResults().stream()
				.map(movieData -> {

					// `poster_path`가 `null`일 경우 기본값 설정
					String imgPath = movieData.getPoster_path();
					if (imgPath == null) {
						imgPath = ""; // 또는 적절한 기본값 설정
					} else {
						// `imageBaseUrl`과 `poster_path`를 결합하여 전체 이미지 URL 생성
						imgPath = imageBaseUrl + imgPath;
					}
					return PopularMovie.builder().code(movieData.getId()).title(movieData.getTitle()).img(imgPath)
							.build();
				}).collect(Collectors.toList());

		popuRepo.saveAll(movies);
	}

}
