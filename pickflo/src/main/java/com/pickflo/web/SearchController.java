package com.pickflo.web;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pickflo.dto.SearchMovieListDto;
import com.pickflo.dto.SearchMovieRequestDto;
import com.pickflo.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SearchController {
	
	private final SearchService searchSvc;
	
	@GetMapping("/movie/search")
	public String search(SearchMovieRequestDto dto, Model model) {
		log.info("SearchController(dto={})", dto);
		
		// 키워드가 비어 있는 경우 빈 리스트를 반환
	    if (dto.getKeywords() == null || dto.getKeywords().trim().isEmpty()) {
	        model.addAttribute("searchMovies", Collections.emptyList());
	        return "movie/search";
	    }
		
		List<SearchMovieListDto> result = searchSvc.search(dto);
		model.addAttribute("searchMovies", result);
		
		return "movie/search";
	}
	
}
