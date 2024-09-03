package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.pickflo.dto.UserMovieLikeDto;
import com.pickflo.repository.UserMoviePickRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieLikeService { 
	
	private final UserMoviePickRepository userMoviePickReop;

	//유저가 찜한 영화리스트
	public List<UserMovieLikeDto> getAllMovies(Long userId) {	
		return userMoviePickReop.findByUserId(userId);
	}
	
	//유저가 찜한 상태 보여주기
	public Boolean getLikeStatus(Long userId, Long movieId) {
		boolean isFavorite = userMoviePickReop.existsByUserIdAndMovieId(userId, movieId);
		return isFavorite;
	}
}
