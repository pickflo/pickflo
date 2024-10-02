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
        	        .likeEvent(0)
        	        .unlikeEvent(0)
        	        .lastUpdated(new Timestamp(System.currentTimeMillis())) // 마지막 업데이트 시간도 초기화
        	        .build();
        }
        
        // Null 값에 대한 안전한 처리
        if (userStatistics.getLikeEvent() == null) {
            userStatistics.setLikeEvent(0);
        }
        if (userStatistics.getUnlikeEvent() == null) {
            userStatistics.setUnlikeEvent(0);
        }

        // actionType에 따라 이벤트 횟수 증가
        if ("page_view".equals(userData.getActionType())) {
            userStatistics.setPageView(userStatistics.getPageView() + 1);
        } else if ("scroll".equals(userData.getActionType())) {
            userStatistics.setScrollCount(userStatistics.getScrollCount() + 1);    
        } else if ("like_event".equals(userData.getActionType())) {
        	userStatistics.setLikeEvent(userStatistics.getLikeEvent() + 1);        	
        } else if ("unlike_event".equals(userData.getActionType())) {
        	userStatistics.setUnlikeEvent(userStatistics.getUnlikeEvent() + 1);
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