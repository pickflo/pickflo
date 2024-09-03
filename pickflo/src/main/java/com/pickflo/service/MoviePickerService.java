package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.dto.MoviePickerDto;
import com.pickflo.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoviePickerService {

	private final MovieRepository repo;
	
//	@Transactional(readOnly = true)
//	public List<MoviePickerDto> readMovies(Double rating, int page,int size) {
//		Page<Movie> moviePage  = repo.findMoviesWithRatingAbove(rating, PageRequest.of(page,size));
//		return moviePage .getContent().stream().map(movie -> 
//				MoviePickerDto.builder()
//				.id(movie.getId()).title(movie.getMovieTitle()).img(movie.getMovieImg())
//				.build())
//				.collect(Collectors.toList());
//	}
	
	@Transactional(readOnly = true)
	public List<MoviePickerDto> readRandomMoviesByGenre(double rating) {
		/*
		 * int startRow = page * size; int endRow = startRow + size;
		 */
        List<MoviePickerDto> movies  = repo.findMoviesByGenreAndRating(rating);
 
        //List<MoviePickerDto> movies = moviePage.getContent();
		return movies;
    }
	
//	//정희테스트
//	@Transactional(readOnly = true)
//	public List<Movie> readMovies(int limit) {
//		List<Movie> list= repo.findAllMoviesRandomly();
//		return list.stream().limit(limit).toList();
//
//	}
}
