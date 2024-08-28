package com.pickflo.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.dto.MovieGenreDto;
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
	private String imageBaseUrl; 
	
	/* 장르id: 액션(28), 모험(12), 애니메이션(16), 코미디(35), 범죄(80), 
	 * 다큐멘터리(99), 드라마(18), 가족(10751), 판타지(14), 역사(36), 공포(27), 
	 * 음악(10402), 미스터리(9648), 로맨스(10749), SF(878), 스릴러(53), 전쟁(10752) */
	private String with_genres = "53"; 
	
	@Transactional
    public void fetchMoviesFromPages() {
        for (int i = 5; i <= 20; i++) {
            getMovieId(i);
        }
    }

	public void getMovieId(int pageNumber) {
        List<MovieGenreDto> list = movieClient.getGenreMovies(apiKey, with_genres, language, String.valueOf(pageNumber))
                .getResults().stream()
                .map(MovieData -> {
                    Long id = MovieData.getId();
                    return MovieGenreDto.builder().movieId(id).build();
                })
                .collect(Collectors.toList());

        // forEach를 사용하여 getMovies 호출
        list.forEach(movieGenreDto -> getMovies(movieGenreDto.getMovieId()));
    }

	public void getMovies(Long id) {
		MovieDetailResponse movieData = movieClient.getMovie(apiKey, id, language);

		if (movieData != null) {
			// 영화가 이미 데이터베이스에 있는지 확인
			boolean exists = movieRepo.existsByMovieCode(movieData.getId());

			// 영화가 없으면 저장
			if (!exists) {
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
