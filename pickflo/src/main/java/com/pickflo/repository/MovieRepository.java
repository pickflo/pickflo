package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieQuerydsl {
	// movieCode로 영화 존재 여부 확인
    boolean existsByMovieCode(Long movieCode);
    
    Movie findByMovieCode(Long movieCode);

}