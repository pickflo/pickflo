package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.repository.MoviePickerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoviePickerService {

	private final MoviePickerRepository repo;
	
	@Transactional(readOnly = true)
	public List<Movie> readMovies() {
		List<Movie> list= repo.findAll();
		return list;

	}
	
//	//정희테스트
//	@Transactional(readOnly = true)
//	public List<Movie> readMovies(int limit) {
//		List<Movie> list= repo.findAllMoviesRandomly();
//		return list.stream().limit(limit).toList();
//
//	}
}
