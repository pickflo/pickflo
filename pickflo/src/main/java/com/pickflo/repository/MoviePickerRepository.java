package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.Movie;

public interface MoviePickerRepository extends JpaRepository<Movie, Long>{

}
