package com.pickflo.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.MoviePickerDto;
import com.pickflo.service.MoviePickerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/picker")
public class MoviePickerRestController {

	private final MoviePickerService svc;

	 @GetMapping("/listByGenre")
	 public ResponseEntity<List<MoviePickerDto>> readRandomMoviesByGenre(@RequestParam(defaultValue = "6.0") double rating) {
		 
		List<MoviePickerDto> list = svc.readRandomMoviesByGenre(rating);
        return ResponseEntity.ok(list);
    }
}
