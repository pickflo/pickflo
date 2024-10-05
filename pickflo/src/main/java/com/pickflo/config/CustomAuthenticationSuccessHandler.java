package com.pickflo.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.pickflo.domain.User;
import com.pickflo.domain.UserStatistics;
import com.pickflo.service.UserMoviePickService;
import com.pickflo.service.UserService;
import com.pickflo.service.UserStatisticsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final UserMoviePickService userMoviePickSvc;
	private final UserService userSvc;
	private final UserStatisticsService userStatisticsSvc;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
    		// 로그인한 사용자의 이메일을 통해 User 정보를 가져옴
        String email = authentication.getName();
        User user = userSvc.findByEmail(email);

        int pickedCount = userMoviePickSvc.getPickedCountByUserId(user.getId());

        log.info("User ID: {}", user.getId());
        log.info("Picked Count: {}", pickedCount);
        
        // visitCount 증가
        UserStatistics userStatistics = new UserStatistics();
        String userGroup = (user.getId() % 2 == 0) ? "B" : "A"; // 홀수/짝수 구분
        userStatistics.setUserGroup(userGroup);
        userStatistics.setVisitorCount(1); // 방문 카운트 1 증가

        // 로그인 시에만 통계 저장
        userStatisticsSvc.saveUserData(userStatistics, true); // 로그인 성공 시에만 호출

        if (pickedCount < 3) {
            response.sendRedirect("/pickflo/movie/picker");
        } else {
            response.sendRedirect("/pickflo");
        }
    }

}
