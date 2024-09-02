package com.pickflo.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
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
	
	// 추천 영화 개봉일 정렬
//	public List<HomeRecMovieDto> getMoviesByUserId(Long userId) {
//        List<Object[]> results = homeRecMovieRepo.findMoviesByUserId(userId);
//
//        return results.stream()
//            .map(result -> new HomeRecMovieDto(
//                ((Number) result[0]).longValue(), // movieId
//                (String) result[1], // movieImg
//                convertToLocalDate((Timestamp) result[2]) // movieReleaseDate
//            ))
//            .collect(Collectors.toList());
//    }
//
//    private LocalDate convertToLocalDate(Timestamp timestamp) {
//        return (timestamp != null) ? timestamp.toLocalDateTime().toLocalDate() : null;
//    }
	
	public List<HomeRecMovieDto> getMoviesByUserId(Long userId) {
        List<Object[]> results = homeRecMovieRepo.findMoviesByUserId(userId);

        return results.stream()
            .map(result -> new HomeRecMovieDto(
                ((Number) result[0]).longValue(),
                (String) result[1] 
            ))
            .collect(Collectors.toList());
    }
	
//    public List<HomeRecMovieDto> getHomeRecMovies(Long userId) {
//        return homeRecMovieRepo.findMoviesByUserId(userId);
//    }
	
}
