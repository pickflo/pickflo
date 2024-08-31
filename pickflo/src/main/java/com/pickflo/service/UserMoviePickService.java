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
		List<UserMoviePick> userMoviePicks = dtoList.stream().map(dto -> {

			User user = userRepo.findById(dto.getUserId()).orElseThrow();
			log.info("user: {}", user);

			Movie movie = movieRepo.findById(dto.getMovieId()).orElseThrow();
			log.info("movie: {}", movie);

			UserMoviePick ump = UserMoviePick.builder().userId(user.getId()).movieId(movie.getId()).user(user)
					.movie(movie).build();
			log.info("ump: {}", ump);

			return ump;
		}).collect(Collectors.toList());

		pickRepo.saveAll(userMoviePicks);
	}

	//유저가 찜한 영화의 갯수 확인
	@Transactional
	public int getPickedCountByUserId(Long userId) {
		return pickRepo.countByUserId(userId);
	}
}
