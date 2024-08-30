package com.pickflo.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.UserMoviePickDto;
import com.pickflo.service.UserMoviePickService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usermovie")
public class UserMoviePickRestController {

	private final UserMoviePickService svc;

	@PostMapping("/save")
	public ResponseEntity<?> save(@RequestBody List<UserMoviePickDto> list) {
		try {
			log.info("@@@@@@@@@controller-list=${}",list);

			svc.create(list); 
			return ResponseEntity.ok("Movies saved successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error occurred while saving movies");
		}
	}

}
