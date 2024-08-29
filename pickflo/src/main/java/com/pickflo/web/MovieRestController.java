package com.pickflo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.service.CountryService;
import com.pickflo.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/movie")
@RestController
@RequiredArgsConstructor
public class MovieRestController {

	private final MovieService movieSvc;
	private final CountryService countrySvc;

	@GetMapping("/saveMovies")
	public ResponseEntity<String> fetchAndSaveMoviesAndGenres() {
		try {
			movieSvc.fetchAndSaveMoviesAndGenres();
			return ResponseEntity.ok("Movies and genres fetched and saved successfully.");
		} catch (Exception e) {
			log.error("Error occurred while fetching and saving movies and genres: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to fetch and save movies and genres.");
		}
	}

	@GetMapping("/saveCountries")
	public void getCountryCode() {
		countrySvc.getCountryId();
	}

}
