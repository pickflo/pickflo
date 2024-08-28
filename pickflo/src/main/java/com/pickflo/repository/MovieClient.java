package com.pickflo.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonProperty;

@FeignClient(name = "MovieClient", url = "${tmdb.api.url}") // 기본 URL
public interface MovieClient {

	@GetMapping("/discover/movie") // 기본 URL 뒤에 붙일 end point(인기 영화)
	TmdbResponse getGenreMovies(@RequestParam("api_key") String apiKey, @RequestParam String with_genres, @RequestParam String language, @RequestParam String page); // api key와 언어 제한
	
	@GetMapping("/movie/{id}") // 기본 URL 뒤에 붙일 end point(인기 영화)
	MovieDetailResponse getMovie(@RequestParam("api_key") String apiKey, @PathVariable Long id, @RequestParam String language); // api key와 언어 제한
	

	// tmdb api의 인기영화 목록 필드 results
	class TmdbResponse {
		
		@JsonProperty("results")
		private List<MovieData> results;

		public List<MovieData> getResults() {
			return results;
		}
		
		  public void setResults(List<MovieData> results) {
	            this.results = results;
	        }
	}
	
    class MovieDetailResponse { // 영화 상세 정보에 대한 응답 클래스
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
