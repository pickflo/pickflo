package com.pickflo.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Genre;
import com.pickflo.domain.Movie;
import com.pickflo.domain.MovieGenre;
import com.pickflo.repository.GenreRepository;
import com.pickflo.repository.MovieClient;
import com.pickflo.repository.MovieClient.MovieDetailResponse;
import com.pickflo.repository.MovieGenreRepository;
import com.pickflo.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieService {

	private final MovieClient movieClient;
	private final MovieRepository movieRepo;
	private final GenreRepository genreRepo;
	private final MovieGenreRepository movieGenreRepo;

	@Value("${tmdb.api.key}")
	private String apiKey;

	@Value("${tmdb.language}")
	private String language;

	@Value("${tmdb.image.base.url}")
	private String imageBaseUrl;

	/*
	 * 장르id: 액션(28), 모험(12), 애니메이션(16), 코미디(35), 범죄(80), 다큐멘터리(99), 드라마(18),
	 * 가족(10751), 판타지(14), 역사(36), 공포(27), 음악(10402), 미스터리(9648), 로맨스(10749),
	 * SF(878), 스릴러(53), 전쟁(10752)
	 */
	private String with_genres = "10402";

	/* 대한민국(KR), 미국(US), 대만(TW), 일본(JP), 중국(ㅊ */
	private String with_origin_country = "KR";

	@Transactional
	public void fetchAndSaveMoviesAndGenres() {
		for (int i = 22; i <= 22; i++) {
			List<Long> movieIds = getMovieIds(i);
			movieIds.forEach(this::getAndSaveMovieAndGenres);
		}
	}

	public List<Long> getMovieIds(int pageNumber) {
		return movieClient.getGenreMovies(apiKey, with_genres, language, String.valueOf(pageNumber)).getResults()
				.stream().map(MovieClient.MovieIdResponse::getId).collect(Collectors.toList());
	}

	public void getAndSaveMovieAndGenres(Long id) {
		MovieDetailResponse movieData = movieClient.getMovie(apiKey, id, language);

		if (movieData != null) {
			boolean exists = movieRepo.existsByMovieCode(movieData.getId());

			if (!exists) {
				String imgPath = movieData.getPoster_path() == null ? "" : imageBaseUrl + movieData.getPoster_path();
				Movie movie = Movie.builder().movieCode(movieData.getId()).movieTitle(movieData.getTitle())
						.movieImg(imgPath).movieOverview(movieData.getOverview())
						.movieRating(movieData.getVote_average())
						.movieReleaseDate(LocalDate.parse(movieData.getRelease_date()))
						.movieRuntime(movieData.getRuntime()).build();
				movieRepo.save(movie);

				saveMovieGenres(movie.getId(), movieData.getGenres());
			} else {
				log.info("Movie with code {} already exists in the database.", movieData.getId());
			}
		}
	}

	private void saveMovieGenres(Long movieId, List<MovieClient.Genre> genres) {
		for (MovieClient.Genre genre : genres) {
			Genre genreEntity = genreRepo.findByGenreCode(genre.getId());
			if (genreEntity != null) {
				Long genreId = genreEntity.getId();
				MovieGenre movieGenre = MovieGenre.builder().movieId(movieId).genreId(genreId).build();
				try {
					movieGenreRepo.save(movieGenre);
				} catch (Exception e) {
					log.error("Error saving movie genre with movieId {} and genreId {}", movieId, genreId, e);
				}
			} else {
				log.error("Genre with id {} not found", genre.getId());
			}
		}
	}
}
