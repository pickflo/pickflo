package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{
	// movieCode로 영화 존재 여부 확인
    boolean existsByMovieCode(Long movieCode);
    
}
