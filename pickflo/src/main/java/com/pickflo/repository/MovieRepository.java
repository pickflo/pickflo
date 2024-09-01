package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;

import com.pickflo.dto.SearchGenreCountryDto;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, MovieQuerydsl {
	// movieCode로 영화 존재 여부 확인
	boolean existsByMovieCode(Long movieCode);
    
    Movie findByMovieCode(Long movieCode);

    @Query("select new com.pickflo.dto.SearchGenreCountryDto(m.movieCode, m.movieImg) "
            + "from Movie m "
            + "join m.movieGenres mg "
            + "where mg.genre.id = :genreId")
      List<SearchGenreCountryDto> findMoviesByGenreId(@Param("genreId") Long genreId);

}