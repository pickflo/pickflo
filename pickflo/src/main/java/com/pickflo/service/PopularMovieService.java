package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.SurveyMovie;
import com.pickflo.repository.PopularMovieRepository;
import com.pickflo.repository.PopularMovieClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PopularMovieService {

	private final PopularMovieClient popularMovieClient;
	private final PopularMovieRepository movieRepo;

	// resource/application.properties에 저장한 정보 가져옴
	@Value("${tmdb.api.key}")
	private String apiKey;

	@Value("${tmdb.language}")
	private String language;

	@Value("${tmdb.image.base.url}")
	private String imageBaseUrl; // 이미지 기본 URL

	@Transactional
	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 인기영화 데이터 업데이트
	public void updatePopularMovies() {
		// 기존 데이터를 삭제
		movieRepo.deleteAll();

		List<SurveyMovie> movies = popularMovieClient.getPopularMovies(apiKey, language).getResults().stream()
				.map(movieData -> {
					// poster_path(img URL) 데이터 불러오기
					// `imageBaseUrl`과 `poster_path`를 합쳐서 전체 이미지 URL 생성
					// bean img
					String imgPath = movieData.getPoster_path() == null ? "" : imageBaseUrl + movieData.getPoster_path();
				
					return SurveyMovie.builder().movieId(movieData.getId()).title(movieData.getTitle()).img(imgPath)
							.build();
				}).collect(Collectors.toList());

		movieRepo.saveAll(movies);
	}
	
	@Transactional(readOnly = true)
	public List<SurveyMovie> readPopularMovies() {
		List<SurveyMovie> list= movieRepo.findAll();
		return list;
		
	}

}
