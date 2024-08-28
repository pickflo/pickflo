package com.pickflo.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.domain.SurveyMovie;
import com.pickflo.dto.MovieGenreDto;
import com.pickflo.repository.MovieClient;
import com.pickflo.repository.MovieRepository;
import com.pickflo.repository.PopularMovieClient;
import com.pickflo.repository.PopularMovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieService {
	
	private final MovieClient movieClient;
	private final MovieRepository movieRepo;
	
	@Value("${tmdb.api.key}")
	private String apiKey;
	
	@Value("${tmdb.language}")
	private String language;
	
	@Value("${tmdb.image.base.url}")
	private String imageBaseUrl; // 이미지 기본 URL
	
	private String with_genres="28";
	
	@Transactional
	public void getMovieId() {
		// 기존 데이터를 삭제
		movieRepo.deleteAll();

		List<MovieGenreDto> list = movieClient.getMovies(apiKey, with_genres, language).getResults().stream()
				.map(movieData -> {
					// poster_path(img URL) 데이터 불러오기
					// `imageBaseUrl`과 `poster_path`를 합쳐서 전체 이미지 URL 생성
					// bean img
					Long id= movieData.getId();
					return MovieGenreDto.builder().movieId(id).build();
				}).collect(Collectors.toList());
		
		 // forEach를 사용하여 getMovies 호출
	    list.forEach(movieGenreDto -> getMovies(movieGenreDto.getMovieId()));
		
	}
	
	public void getMovies(Long id) {
		
		/* private String id=id; */
		
		List<Movie> list = movieClient.getMovies(apiKey, id.toString(), language).getResults().stream()
				.map(movieData -> {
					String imgPath = movieData.getPoster_path() == null ? "" : imageBaseUrl + movieData.getPoster_path();

					return Movie.builder().movieTitle(movieData.getTitle())
							.movieImg(imgPath).movieOverview(movieData.getOverview())
							.movieRating(movieData.getVote_average())
							.movieReleaseDate(Date.valueOf(movieData.getRelease_date()))
							.movieRuntime(movieData.getRuntime())
							.build();
				}).collect(Collectors.toList());
        movieRepo.saveAll(list);
	}
}
