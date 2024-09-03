package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.dto.MoviePickerDto;
import com.pickflo.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoviePickerService {

	private final MovieRepository repo;
	
	@Transactional(readOnly = true)
	public List<MoviePickerDto> readRandomMoviesByGenre(double rating) {
        List<MoviePickerDto> movies  = repo.findMoviesByGenreAndRating(rating);
 
		return movies;
    }
	
}
