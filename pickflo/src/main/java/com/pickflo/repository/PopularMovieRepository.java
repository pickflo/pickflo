package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.SurveyMovie;

public interface PopularMovieRepository extends JpaRepository<SurveyMovie, Long>{

}
