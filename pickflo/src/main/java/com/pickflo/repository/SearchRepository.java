package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.Movie;
import com.pickflo.dto.SearchGenreDto;


public interface SearchRepository extends JpaRepository<Movie, Long> {
	
	@Query("select new com.pickflo.dto.SearchGenreDto(m.movieCode, m.movieImg) "
            + "from Movie m "
            + "join MovieGenre mg on m.id = mg.movieId "
            + "join Genre g on mg.genreId = g.id "
            + "where g.genreCode = :genreCode")
    List<SearchGenreDto> findMoviesByGenreCode(@Param("genreCode") Integer genreCode);
}