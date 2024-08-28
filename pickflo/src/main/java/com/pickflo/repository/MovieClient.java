package com.pickflo.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tmdbClient", url = "${tmdb.api.url}") // 기본 URL
public interface MovieClient {

	@GetMapping("/discover/movie") // 기본 URL 뒤에 붙일 end point(인기 영화)
	TmdbResponse getMovies(@RequestParam("api_key") String apiKey, @RequestParam String with_genres, @RequestParam String language); // api key와 언어 제한
	
	@GetMapping("/movie") // 기본 URL 뒤에 붙일 end point(인기 영화)
	TmdbResponse getMovies(@RequestParam("api_key") String apiKey, @PathVariable Long id, @RequestParam String language); // api key와 언어 제한
	

	// tmdb api의 인기영화 목록 필드 results
	class TmdbResponse {
		private List<MovieData> results;

		public List<MovieData> getResults() {
			return results;
		}
	}

	class MovieData {
		private Long id;
		private String title;
		private String poster_path;
		private String overview;
		private Double vote_average;
		private String release_date;
		private Integer runtime;
		
		public Long getId() {
			return id;
		}
		public String getTitle() {
			return title;
		}
		public String getPoster_path() {
			return poster_path;
		}
		public String getOverview() {
			return overview;
		}
		public Double getVote_average() {
			return vote_average;
		}
		public String getRelease_date() {
			return release_date;
		}
		public Integer getRuntime() {
			return runtime;
		}

		
	}

}
