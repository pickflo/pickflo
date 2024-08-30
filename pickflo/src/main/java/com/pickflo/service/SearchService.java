package com.pickflo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pickflo.dto.SearchGenreDto;
import com.pickflo.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SearchService {
	private final SearchRepository searchRepo;

	public List<SearchGenreDto> findMoviesByGenreAndCountryCode(Integer genreCode, String countryCode) {
        return searchRepo.findMoviesByGenreAndCountryCode(genreCode, countryCode);
    }
}
