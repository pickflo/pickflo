package com.pickflo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.service.MovieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/movie")
@RestController
@RequiredArgsConstructor
public class MovieRestController {

	
	private final MovieService movieSvc;
	
	@GetMapping("/save/movies")
    public ResponseEntity<String> fetchAndSaveMovies() {
        try {
            movieSvc.fetchMoviesFromPages(); 
            return ResponseEntity.ok("Movies fetched and saved successfully.");
        } catch (Exception e) {
            log.error("Error occurred while fetching and saving movies: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch and save movies.");
        }
    }
	
	@GetMapping("/save/genres")
	public ResponseEntity<String> updateAllMovieGenres() {
        try {
            movieSvc.updateAllMovieGenres();
            return ResponseEntity.ok("All movie genres updated successfully.");
        } catch (Exception e) {
            log.error("Error occurred while updating all movie genres: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update all movie genres.");
        }
    }
}
