package com.pickflo.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.repository.MovieClient;
import com.pickflo.repository.MovieClient.MovieDetailResponse;
import com.pickflo.repository.MovieRepository;

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

	private String with_genres = "16";

	@Transactional
	public void getMovieId() {
		List<Long> list = movieClient.getGenreMovies(apiKey, with_genres, language).getResults().stream()
				.map(MovieData -> MovieData.getId()).collect(Collectors.toList());

		// 각 영화 ID에 대해 getMovies 함수 호출
		list.forEach(this::getMovies);
	}

	public void getMovies(Long id) {
		MovieDetailResponse movieData = movieClient.getMovie(apiKey, id, language);

		if (movieData != null) {
			// 영화가 이미 데이터베이스에 있는지 확인
			boolean exists = movieRepo.existsByMovieCode(movieData.getId());

			// 영화가 없으면 저장
			if (!exists) {
				// imgPath는 imageBaseUrl + poster_path
				String imgPath = movieData.getPoster_path() == null ? "" : imageBaseUrl + movieData.getPoster_path();
				Movie movie = Movie.builder().movieCode(movieData.getId()).movieTitle(movieData.getTitle())
						.movieImg(imgPath).movieOverview(movieData.getOverview())
						.movieRating(movieData.getVote_average())
						.movieReleaseDate(Date.valueOf(movieData.getRelease_date()))
						.movieRuntime(movieData.getRuntime()).build();
				movieRepo.save(movie);
			} else {
				log.info("Movie with code {} already exists in the database.", movieData.getId());
			}
		}

	}

}
