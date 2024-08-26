package com.pickflo.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickflo.domain.Popular;
import com.pickflo.repository.PopularRepository;
import com.pickflo.repository.TmdbClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PopularService {

	private final TmdbClient tmdbClient;
	private final PopularRepository popRepo;
	private final ObjectMapper objectMapper;

	@Value("${tmdb.api.key}")
	private String apiKey;

	@Value("${tmdb.api.url}")
	private String apiUrl;

	@Transactional
	public void fetchAndSavePopularMovies() {
		String response = tmdbClient.getPopularMovies(apiKey);

		try {
			JsonNode rootNode = objectMapper.readTree(response);
			JsonNode resultsNode = rootNode.path("results");

			for (JsonNode movieNode : resultsNode) {
				Long movieId = movieNode.path("id").asLong();
				String title = movieNode.path("title").asText();
				String posterPath = movieNode.path("poster_path").asText();
				String imageUrl = "https://image.tmdb.org/t/p/original" + posterPath;

				Popular movie = new Popular();
				movie.setCode(movieId); // TMDB ID 저장
				movie.setTitle(title);
				movie.setImg(imageUrl);
				log.debug("-------------title={}",title);

				popRepo.save(movie); // Use popRepo instead of movieRepository
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
