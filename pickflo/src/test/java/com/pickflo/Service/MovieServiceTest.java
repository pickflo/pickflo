package com.pickflo.Service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pickflo.dto.MovieDetailsDto;
import com.pickflo.service.MovieService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MovieServiceTest {
	
	@Autowired
	private MovieService movieSvc;
	
	@Test
    public void testGetMovieDetails() {
        Long movieId = 189L;
        Optional<MovieDetailsDto> dto = movieSvc.getMovieDetails(movieId);
        
        if (dto.isPresent()) {
            log.info("Movie Details: {}", dto.toString());
        } 
        
    }
}
