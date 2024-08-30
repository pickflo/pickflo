package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.UserMoviePick;

public interface UserMoviePickRepository extends JpaRepository<UserMoviePick, Long>{

}
