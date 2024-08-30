package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.domain.User;
import com.pickflo.domain.UserMoviePick;
import com.pickflo.dto.UserMoviePickDto;
import com.pickflo.repository.MovieRepository;
import com.pickflo.repository.UserMoviePickRepository;
import com.pickflo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMoviePickService {

	private final UserMoviePickRepository pickRepo;
	private final UserRepository userRepo;
	private final MovieRepository movieRepo;

	@Transactional
	public void create(List<UserMoviePickDto> dtoList) {
		log.info("******************");
		List<UserMoviePick> userMoviePicks = dtoList.stream().map(dto -> {
			
			User user = userRepo.findById(dto.getUserId()).orElseThrow();
			log.info("user: {}", user);
			
			Movie movie = movieRepo.findByMovieCode(dto.getMovieId()).orElseThrow();
			log.info("movie: {}", movie);
		
			UserMoviePick ump = UserMoviePick.builder().user(user).movie(movie).build();
			log.info("ump: {}", ump);
			
			return ump;
		}).collect(Collectors.toList());
		
		log.info("^^^^^^^^^^^^^^^{}",userMoviePicks);

		pickRepo.saveAll(userMoviePicks);
	}
}
