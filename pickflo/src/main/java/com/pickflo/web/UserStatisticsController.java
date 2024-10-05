package com.pickflo.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pickflo.domain.UserStatistics;
import com.pickflo.service.UserStatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-statistics")
@RequiredArgsConstructor
public class UserStatisticsController {
	
	private final UserStatisticsService userStatisticsSvc;

	@PostMapping("/saveUserData")
    public ResponseEntity<Void> saveUserData(@RequestBody UserStatistics userData) {
        try {
            userStatisticsSvc.saveUserData(userData, true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 예외 처리
            System.err.println("Error saving user data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

  
    @GetMapping("/getUserData")
    public ResponseEntity<List<UserStatistics>> getUserData() {
        List<UserStatistics> data = userStatisticsSvc.getUserStatistics();
        return ResponseEntity.ok(data);
    } 

}
