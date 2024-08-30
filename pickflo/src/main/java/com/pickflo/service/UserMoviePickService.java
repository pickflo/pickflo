package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.domain.User;
import com.pickflo.domain.UserMoviePick;
import com.pickflo.dto.UserMoviePickDto;
import com.pickflo.repository.UserMoviePickRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMoviePickService {

	private final UserMoviePickRepository repo;

	@Transactional
	public void create(List<UserMoviePickDto> list) {
		for (UserMoviePickDto dto : list) {
			UserMoviePick result = UserMoviePick.builder().userId(dto.getUserId()).movieId(dto.getMovieId()).build();
			repo.save(result);
		}
	}
}
