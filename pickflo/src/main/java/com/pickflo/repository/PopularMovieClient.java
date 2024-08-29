package com.pickflo.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PopularMovieClient", url = "${tmdb.api.url}") // 기본 URL
public interface PopularMovieClient {

	@GetMapping("/movie/popular") // 기본 URL 뒤에 붙일 end point(인기 영화)
	TmdbResponse getPopularMovies(@RequestParam("api_key") String apiKey, @RequestParam String language); // api key와 언어 제한

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

		public Long getId() {
			return id;
		}

		public String getTitle() {
			return title;
		}

		public String getPoster_path() {
			return poster_path;
		}
	}

}
