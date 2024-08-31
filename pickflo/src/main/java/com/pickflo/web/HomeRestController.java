package com.pickflo.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.HomeRecMovieDto;
import com.pickflo.service.HomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HomeRestController {
	
	private final HomeService homeSvc;

	@GetMapping("/home-recommendations")
    public List<HomeRecMovieDto> getHomeRecommendations(@RequestParam Long userId) {
        return homeSvc.getHomeRecommendations(userId);
    }
}
