package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.Movie;
import com.pickflo.dto.SearchGenreDto;

public interface SearchRepository extends JpaRepository<Movie, Long> {

	@Query("select distinct new com.pickflo.dto.SearchGenreDto(m.movieCode, m.movieImg) " + "from Movie m "
			+ "join MovieGenre mg on m.id = mg.movieId " + "join Genre g on mg.genreId = g.id "
			+ "join MovieCountry mc on m.id = mc.movieId " + "join Country c on mc.countryId = c.id "
			+ "where (:genreCode is null or g.genreCode = :genreCode) "
			+ "and (:countryCode is null or c.countryCode = :countryCode)")
	List<SearchGenreDto> findMoviesByGenreAndCountryCode(@Param("genreCode") Integer genreCode,
			@Param("countryCode") String countryCode);
}
