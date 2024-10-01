package com.pickflo.web;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.dto.ChartDto;
import com.pickflo.service.UserStatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chart")
@RequiredArgsConstructor
public class ChartRestController {

	private final UserStatisticsService svc;

	@PostMapping("/saveUserData")
	public ResponseEntity<Void> saveUserData(@RequestBody ChartDto userData) {
		// DB에 userData 저장하는 로직 구현
		svc.saveUserData(userData);
		return ResponseEntity.ok().build();
	}
	
	// 데이터를 가져오는 API
	/*
	 * @GetMapping("/getUserData") public ResponseEntity<List<ChartDto>>
	 * getUserData() { // DB에서 유저 데이터 가져오는 로직 구현 List<ChartDto> data = return
	 * ResponseEntity.ok(data); }
	 */
}
