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
public interface MovieRepository extends JpaRepository<Movie, Long>{
	// movieCode로 영화 존재 여부 확인
	boolean existsByMovieCode(Long movieCode);
    
    Movie findByMovieCode(Long movieCode);

    @Query("select new com.pickflo.dto.SearchGenreCountryDto(m.movieCode, m.movieImg) "
            + "from Movie m "
            + "join m.movieGenres mg "
            + "where mg.genre.id = :genreId")
      List<SearchGenreCountryDto> findMoviesByGenreId(@Param("genreId") Long genreId);
    
    // 평점이 6점 이상인 영화만 조회하는 메서드
    @Query("select m from Movie m where m.movieRating >= :rating")
    Page<Movie> findMoviesWithRatingAbove(Double rating, Pageable pageable);
    
//    @Query(value = "WITH RankedMovies AS (" +
//    		"    SELECT m.movie_id, m.movie_title, m.movie_img, g.genre_id, m.movie_rating, " +
//    		"           ROW_NUMBER() OVER (PARTITION BY g.genre_id ORDER BY m.movie_rating DESC) AS row_num " +
//    		"    FROM movies m " +
//    		"    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
//    		"    JOIN genres g ON mg.genre_id = g.genre_id " +
//    		"    WHERE m.movie_rating >= 6.0 " +
//    		") " +
//    		"SELECT distinct rm.movie_id, rm.movie_title, rm.movie_img, ge.genre_name, rm.movie_rating " +
//    		"FROM RankedMovies rm " +
//    		"JOIN genres ge ON rm.genre_id = ge.genre_id " +
//    		"WHERE rm.row_num <= 10 " +
//    		"ORDER BY ge.genre_name, rm.movie_rating DESC", 
//    		nativeQuery = true)
//    List<MoviePickerDto> findMoviesByGenreAndRating();
    
    @Query(value = "SELECT * FROM ( " +
    	    "    SELECT m.movie_id AS movieId, m.movie_title AS movieTitle, m.movie_img AS movieImg, " +
    	    "           g.genre_name AS genreName, m.movie_rating AS movieRating, " +
    	    "           ROW_NUMBER() OVER (PARTITION BY g.genre_id ORDER BY DBMS_RANDOM.VALUE) AS rn " +
    	    "    FROM movies m " +
    	    "    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
    	    "    JOIN genres g ON mg.genre_id = g.genre_id " +
    	    "    WHERE m.movie_rating >= :rating " +
//    	    "    AND m.movie_id NOT IN :excludedMovieIds " +  // 이미 로드된 영화 제외
    	    "	AND (:excludedMovieIds IS NULL OR m.movie_id NOT IN (:excludedMovieIds))" +
    	    ") WHERE rn =1" +
    	    "OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY",
            nativeQuery = true)
    	List<MoviePickerDto> findMoviesByGenreAndRating(
    			@Param("rating") double rating, @Param("excludedMovieIds")List<Long> excludedMovieIds,
    			@Param("offset") int offset, @Param("limit") int limit);
    
  }