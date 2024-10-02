package com.pickflo.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pickflo.domain.UserStatistics;
import com.pickflo.dto.ChartDto;
import com.pickflo.repository.UserStatisticsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

	private final UserStatisticsRepository repo;
	
	public void saveUserData(ChartDto userData) {
        // 사용자 그룹에 따른 통계 가져오기
        UserStatistics userStatistics = repo.findByUserGroup(userData.getUserGroup());

        if (userStatistics == null) {
            // 새로운 통계 생성
        	userStatistics = UserStatistics.builder()
        	        .userGroup(userData.getUserGroup())
        	        .pageView(0)
        	        .scrollCount(0)
        	        .lastUpdated(new Timestamp(System.currentTimeMillis())) // 마지막 업데이트 시간도 초기화
        	        .build();
        }

		// actionType에 따라 페이지 방문 또는 스크롤 횟수 증가
		if ("page_view".equals(userData.getActionType())) {
			userStatistics.setPageView(userStatistics.getPageView() + 1);
		} else if ("scroll".equals(userData.getActionType())) {
			userStatistics.setScrollCount(userStatistics.getScrollCount() + 1);
		} else if ("like".equals(userData.getActionType())) {
			userStatistics.setLikeCount(userStatistics.getLikeCount()+1);
		}else if ("unlike".equals(userData.getActionType())) {
			userStatistics.setUnlikeCount(userStatistics.getUnlikeCount()+1);
		}

        // 마지막 업데이트 시간 갱신
        userStatistics.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        // DB에 저장
        repo.save(userStatistics);
    }

	public List<UserStatistics> getUserStatistics() {
		return repo.findAll();
	}
}
