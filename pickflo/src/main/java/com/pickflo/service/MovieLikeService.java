package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pickflo.dto.UserMovieLikeDto;
import com.pickflo.repository.UserMovieLikeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieLikeService { 
	
	private final UserMovieLikeRepository userMovieLikeReop;

	//유저가 찜한 영화리스트
	public List<UserMovieLikeDto> getAllMovies(Long userId) {	
		return userMovieLikeReop.findByUserId(userId);
	}
}
