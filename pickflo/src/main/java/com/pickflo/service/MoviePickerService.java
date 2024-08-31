package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.dto.MoviePickerDto;
import com.pickflo.repository.MoviePickerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoviePickerService {

	private final MoviePickerRepository repo;

	@Transactional(readOnly = true)
	public List<MoviePickerDto> readMovies(int limit) {
		Page<Movie> page = repo.findAll(PageRequest.of(0, limit));
		return page.getContent().stream().map(movie -> 
				MoviePickerDto.builder()
				.id(movie.getId()).title(movie.getMovieTitle()).img(movie.getMovieImg())
				.build())
				.collect(Collectors.toList());
	}
}
