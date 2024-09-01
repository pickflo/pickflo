package com.pickflo.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Country;
import com.pickflo.domain.Genre;
import com.pickflo.domain.Movie;
import com.pickflo.domain.MovieCountry;
import com.pickflo.domain.MovieGenre;
import com.pickflo.dto.MovieDetailsDto;
import com.pickflo.repository.CountryRepository;
import com.pickflo.repository.GenreRepository;
import com.pickflo.repository.MovieClient;
import com.pickflo.repository.MovieCountryRepository;
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
	private final CountryRepository countryRepo;
	private final MovieGenreRepository movieGenreRepo;
	private final MovieCountryRepository movieCountryRepo;

	@Value("${tmdb.api.key}")
	private String apiKey;

	@Value("${tmdb.language}")
	private String language;

	@Value("${tmdb.image.base.url}")
	private String imageBaseUrl;

	/*
	 * 액션(28), 모험(12), 애니메이션(16), 코미디(35), 범죄(80), 다큐멘터리(99), 드라마(18), 가족(10751),
	 * 판타지(14), 역사(36), 공포(27), 음악(10402), 미스터리(9648), 로맨스(10749), SF(878), 스릴러(53),
	 * 전쟁(10752)
	 */

	/* 대한민국(KR), 미국(US), 대만(TW), 일본(JP), 중국(CN) */
	// 장르와 국가 데이터 배열 정의
	private final String[] with_genres = { "10751", "14", "36", "27", "10402", "9648", "10749", "878", "53", "10752" };
	private final String[] with_origin_country = { "KR", "TW", "CN" };

	@Transactional
	public void saveMoviesByGenres() {
		for (String genreCode : with_genres) {
			for (int page = 1; page <= 5; page++) {
				List<Long> movieIds = getMovieIdsByGenre(genreCode, page);
				movieIds.forEach(this::getAndSaveMovieAndGenres);
			}
		}
	}

	@Transactional
	public void saveMoviesByCountries() {
		for (String countryCode : with_origin_country) {
			for (int page = 6; page <= 20; page++) {
				List<Long> movieIds = getMovieIdsByCountry(countryCode, page);
				movieIds.forEach(this::getAndSaveMovieAndGenres);
			}
		}
	}

	public List<Long> getMovieIdsByGenre(String genreCode, int pageNumber) {
		return movieClient.getGenreMovies(apiKey, genreCode, language, String.valueOf(pageNumber)).getResults().stream()
				.map(MovieClient.MovieIdResponse::getId).collect(Collectors.toList());
	}

	public List<Long> getMovieIdsByCountry(String countryCode, int pageNumber) {
		return movieClient.getCountryMovies(apiKey, countryCode, language, String.valueOf(pageNumber)).getResults()
				.stream().map(MovieClient.MovieIdResponse::getId).collect(Collectors.toList());
	}

//    @Transactional
//    public void fetchAndSaveMoviesAndGenres() {
//        for (String genreCode : with_genres) {
//            for (String countryCode : with_origin_country) {
//                for (int page = 1; page <= 5; page++) {
//                    List<Long> movieIds = getMovieIds(genreCode, countryCode, page);
//                    movieIds.forEach(this::getAndSaveMovieAndGenres);
//                }
//            }
//        }
//    }
//
//    public List<Long> getMovieIds(String genreCode, String countryCode, int pageNumber) {
//        return movieClient.getGenreMovies(apiKey, genreCode, language, String.valueOf(pageNumber))
//                .getResults().stream().map(MovieClient.MovieIdResponse::getId).collect(Collectors.toList());
//    }

	public void getAndSaveMovieAndGenres(Long id) {
		MovieDetailResponse movieData = movieClient.getMovie(apiKey, id, language);

		if (movieData != null) {
			boolean exists = movieRepo.existsByMovieCode(movieData.getId());

			if (!exists) {
				String imgPath = movieData.getPoster_path() == null ? "" : imageBaseUrl + movieData.getPoster_path();
				LocalDate releaseDate = parseReleaseDate(movieData.getRelease_date());

				Movie movie = Movie.builder().movieCode(movieData.getId()).movieTitle(movieData.getTitle())
						.movieImg(imgPath).movieOverview(movieData.getOverview())
						.movieRating(movieData.getVote_average()).movieReleaseDate(releaseDate)
						.movieRuntime(movieData.getRuntime()).build();
				movieRepo.save(movie);

				saveMovieGenres(movie.getId(), movieData.getGenres());
				saveMovieCountries(movie.getId(), movieData.getOriginCountry());
			} else {
				log.info("MovieUserRepository with code {} already exists in the database.", movieData.getId());
			}
		}
	}

	private LocalDate parseReleaseDate(String releaseDateStr) {
		if (releaseDateStr == null || releaseDateStr.trim().isEmpty()) {
			// Return a default date or null based on your needs
			return null; // Change this to a default date if preferred
		}
		try {
			return LocalDate.parse(releaseDateStr);
		} catch (DateTimeParseException e) {
			log.error("Error parsing release date '{}'", releaseDateStr, e);
			// Return null or a default date based on your needs
			return null; // Change this to a default date if preferred
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

	private void saveMovieCountries(Long movieId, List<String> originCountries) {
		for (String countryCode : originCountries) {
			Country countryEntity = countryRepo.findByCountryCode(countryCode);

			if (countryEntity == null) {
				// 국가가 존재하지 않으면 새로 생성
				countryEntity = Country.builder().countryCode(countryCode).countryName("Unknown") // 이름을 적절히 설정하는 방법 필요
						.build();
				countryRepo.save(countryEntity);
			}

			// 영화와 국가의 관계를 저장
			MovieCountry movieCountry = MovieCountry.builder().movieId(movieId).countryId(countryEntity.getId())
					.build();

			try {
				movieCountryRepo.save(movieCountry);
			} catch (Exception e) {
				log.error("Error saving movie country with movieId {} and countryCode {}", movieId, countryCode, e);
			}
		}
	}
	
	public Optional<MovieDetailsDto> getMovieDetails(Long movieId) {
        return movieRepo.findMovieDetailsById(movieId);
    }

}
