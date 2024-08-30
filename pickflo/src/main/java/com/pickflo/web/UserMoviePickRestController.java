package com.pickflo.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.Movie;
import com.pickflo.domain.User;
import com.pickflo.domain.UserMoviePick;
import com.pickflo.dto.UserMoviePickDto;
import com.pickflo.repository.MovieRepository;
import com.pickflo.repository.UserRepository;
import com.pickflo.service.UserMoviePickService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usermovie")
public class UserMoviePickRestController {
	
	private final UserMoviePickService svc;
	private final UserRepository userRepo;
	private final MovieRepository movieRepo;
	
	@PostMapping("/save")
	public ResponseEntity<List<UserMoviePick>> save(@RequestBody List<UserMoviePickDto> list){
		List<UserMoviePick> resultList=null;
		for(UserMoviePickDto dto:list) {
			  User user = userRepo.findById(dto.getUserId()).orElseThrow();
		      Movie  movie = movieRepo.findById(dto.getMovieId()).orElseThrow();
			UserMoviePick result=UserMoviePick.builder()
			.user(user).movie(movie).build();
			resultList.add(result);
			svc.create(result);

		}
		return ResponseEntity.ok(resultList);
	}

}
