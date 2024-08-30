package com.pickflo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.UserMoviePick;
import com.pickflo.repository.UserMoviePickRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMoviePickService {

	private final UserMoviePickRepository repo;
	
	@Transactional
	public void create(UserMoviePick movie) {
		repo.save(movie);
	}
}
