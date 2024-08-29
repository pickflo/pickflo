package com.pickflo.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.SearchGenreDto;
import com.pickflo.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchRestController {

	private final SearchService searchSvc;
	
	@GetMapping("/genre")
    public ResponseEntity<?> searchMoviesByGenreCode(@RequestParam Integer genreCode) {
        log.info("Received request for genreCode: {}", genreCode); 
        try {
            List<SearchGenreDto> movies = searchSvc.findMoviesByGenreCode(genreCode);
            if (!movies.isEmpty()) {
                return ResponseEntity.ok(movies);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found for this genre");
            }
        } catch (Exception e) {
            log.error("Error occurred while searching movies by genre code: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
