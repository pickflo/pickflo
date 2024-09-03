package com.pickflo.web;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.CustomUserDetails;
import com.pickflo.dto.HomeRecMovieDto;
import com.pickflo.service.HomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeRestController {
	
	private final HomeService homeSvc;	
	/*
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/recMovies")
    public ResponseEntity<?> homeRecMovies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Long userId = ((CustomUserDetails) userDetails).getId();
            
            Page<HomeRecMovieDto> moviePage = homeSvc.getMoviesByUserId(userId, page, limit);
            if (moviePage.hasContent()) {
                return ResponseEntity.ok(moviePage);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No movies found for the given criteria");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching movies");
        } 
    } 
    */
	
	@PreAuthorize("isAuthenticated()")
    @GetMapping("/recMovies")
    public List<HomeRecMovieDto> homeRecMovies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = ((CustomUserDetails) userDetails).getId();

        return homeSvc.getMoviesByUserId(userId);
    } 
	
}

