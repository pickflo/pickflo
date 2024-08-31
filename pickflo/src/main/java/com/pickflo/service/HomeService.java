package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pickflo.dto.HomeRecMovieDto;
import com.pickflo.repository.HomeRecMovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
	
	private final HomeRecMovieRepository homeRecMovieRepo;

    public List<HomeRecMovieDto> getHomeRecommendations(Long userId) {
        return homeRecMovieRepo.findMoviesByUserId(userId);
    }
}
