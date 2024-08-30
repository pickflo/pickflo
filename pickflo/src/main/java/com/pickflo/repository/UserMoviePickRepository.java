package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.UserMoviePick;
import com.pickflo.domain.UserMoviePickId;

public interface UserMoviePickRepository extends JpaRepository<UserMoviePick,UserMoviePickId> {

}
