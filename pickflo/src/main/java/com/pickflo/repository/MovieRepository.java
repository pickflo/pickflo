package com.pickflo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;
import com.pickflo.dto.MoviePickerDto;

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
    
    @Query(value = "WITH RankedMovies AS ( " +
            "    SELECT m.movie_id AS movieId, " +
            "           m.movie_title AS movieTitle, " +
            "           m.movie_img AS movieImg, " +
            "           g.genre_name AS genreName, " +
            "           m.movie_rating AS movieRating, " +
            "           ROW_NUMBER() OVER (PARTITION BY g.genre_name ORDER BY DBMS_RANDOM.VALUE) AS rn " +
            "    FROM movies m " +
            "    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
            "    JOIN genres g ON mg.genre_id = g.genre_id " +
            "    WHERE m.movie_rating >= :rating " +
            ") " +
            "SELECT movieId, movieTitle, movieImg, genreName, movieRating " +
            "FROM RankedMovies " +
            "WHERE rn <= 10 " +
            "ORDER BY genreName, rn", 
    nativeQuery = true)
    List<MoviePickerDto> findMoviesByGenreAndRating(@Param("rating") double rating);

}
