package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {

	boolean existsByCountryCode(String countryCode);
	
	@Query("select c.id from Country c where c.countryCode = :countryCode")
    Country findByCountryCode(@Param("countryCode") String countryCode);
}
