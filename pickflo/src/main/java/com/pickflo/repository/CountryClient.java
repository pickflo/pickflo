package com.pickflo.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pickflo.repository.MovieClient.TmdbResponse;

@FeignClient(name = "CountryClient", url = "${tmdb.api.url}") // 기본 URL
public interface CountryClient {

	@GetMapping("configuration/countries") // 기본 URL 뒤에 붙일 end point(국가)
	TmdbResponse getGenreMovies(@RequestParam("api_key") String apiKey, @RequestParam String language);
}
