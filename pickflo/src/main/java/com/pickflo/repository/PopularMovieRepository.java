package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.PopularMovie;

public interface PopularMovieRepository extends JpaRepository<PopularMovie, Long>{

}
