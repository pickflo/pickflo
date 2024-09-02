package com.pickflo.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

//	@GetMapping("/list")
//	public ResponseEntity<List<MoviePickerDto>> readMovies(  @RequestParam(defaultValue = "0") int page, 
//            @RequestParam(defaultValue = "20") int size) {
//		Double rating=6.0;
//		List<MoviePickerDto> list = svc.readMovies(rating,page,size);
//		return ResponseEntity.ok(list);
//	}
	
	 @GetMapping("/listByGenre")
	 public ResponseEntity<List<MoviePickerDto>> readRandomMoviesByGenre(
			 @RequestParam(defaultValue = "6.0") double rating, 
				/*
				 * @RequestParam(defaultValue = "1") int size,
				 * 
				 * @RequestParam int page,
				 */
		     @RequestParam(required = false) String excludedMovieIds,
		     @RequestParam int offset,
	         @RequestParam int limit
			) {
		 log.info("-----------String={}",excludedMovieIds);
		// 제외할 영화 ID를 리스트로 변환
        List<Long> excludedIds = new ArrayList<>();
        log.info("-----------list={}",excludedIds);
        if (excludedMovieIds != null && !excludedMovieIds.isEmpty()) {
            excludedIds = Arrays.stream(excludedMovieIds.split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
	        
		List<MoviePickerDto> list = svc.readRandomMoviesByGenre(rating, /* size, page, */ excludedIds, offset, limit);
        return ResponseEntity.ok(list);
    }
}
