package com.pickflo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pickflo.dto.HomeRecMovieDto;
import com.pickflo.repository.HomeRecMovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {
	
	private final HomeRecMovieRepository homeRecMovieRepo;
	
	public List<HomeRecMovieDto> getMoviesByUserId(Long userId) {
        List<Object[]> results = homeRecMovieRepo.findMoviesByUserId(userId);

        return results.stream()
            .map(result -> new HomeRecMovieDto(
                ((Number) result[0]).longValue(), // movieId
                (String) result[1] // movieImg
            ))
            .collect(Collectors.toList());
    }
	
//    public List<HomeRecMovieDto> getHomeRecMovies(Long userId) {
//        return homeRecMovieRepo.findMoviesByUserId(userId);
//    }
	
}
