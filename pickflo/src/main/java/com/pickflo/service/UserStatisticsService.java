package com.pickflo.service;

import com.pickflo.domain.UserStatistics;
import com.pickflo.repository.UserStatisticsRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final UserStatisticsRepository userStatisticsRepo;
      
    public void saveUserData(UserStatistics userStatistics, boolean isEventOccurred) {
    	
        LocalDateTime now = LocalDateTime.now();
        LocalDate statDate = now.toLocalDate(); // 현재 날짜를 LocalDate로 변환

        List<UserStatistics> existingStatisticsList = userStatisticsRepo.findByUserGroupAndStatDate(userStatistics.getUserGroup(), statDate);
        
        // 중복된 통계 삭제
        if (existingStatisticsList.size() > 1) {
            existingStatisticsList.forEach(stat -> userStatisticsRepo.delete(stat)); // 모든 중복 삭제
        }
        
        // 기존 통계가 있는 경우 업데이트, 없으면 새로 생성
        if (!existingStatisticsList.isEmpty()) {
            UserStatistics existingStatistics = existingStatisticsList.get(0); // 첫 번째 데이터 사용
            if (isEventOccurred) {
                updateExistingStatistics(existingStatistics, userStatistics);
            }
        } else {
            createNewStatistics(userStatistics, statDate, now);
        }
    }

    private void updateExistingStatistics(UserStatistics existingStatistics, UserStatistics userStatistics) {
    		existingStatistics.setTimeSpent(existingStatistics.getTimeSpent() + userStatistics.getTimeSpent());
        existingStatistics.setScrollCount(existingStatistics.getScrollCount() + userStatistics.getScrollCount());
        existingStatistics.setLikeCount(existingStatistics.getLikeCount() + userStatistics.getLikeCount());
        existingStatistics.setUnlikeCount(existingStatistics.getUnlikeCount() + userStatistics.getUnlikeCount());
        existingStatistics.setVisitorCount(existingStatistics.getVisitorCount() + 1); // 방문 카운트 증가

        // 전환율 계산
        int totalLikes = existingStatistics.getLikeCount();
        int totalVisitors = existingStatistics.getVisitorCount();

        if (totalVisitors > 0) {
            double conversionRate = (double) totalLikes / totalVisitors * 100;
            existingStatistics.setConversionRate(conversionRate);
        } else {
            existingStatistics.setConversionRate(0.0);
        }

        // 업데이트된 데이터 저장
        userStatisticsRepo.save(existingStatistics);
    }

    private void createNewStatistics(UserStatistics userStatistics, LocalDate statDate, LocalDateTime now) {
        UserStatistics newStatistics = UserStatistics.builder()
                .userGroup(userStatistics.getUserGroup())
                .timeSpent(userStatistics.getTimeSpent())
                .scrollCount(userStatistics.getScrollCount())
                .likeCount(userStatistics.getLikeCount())
                .unlikeCount(userStatistics.getUnlikeCount())
                .visitorCount(1) // 방문자 수 초기화
                .conversionRate(0.0) // 초기 전환율 설정
                .statDate(statDate) // LocalDate로 설정
                .createdAt(now) // 현재 시간으로 설정
                .updatedAt(now) // 현재 시간으로 설정
                .build();

        userStatisticsRepo.save(newStatistics);
    }

    public List<UserStatistics> getUserStatistics() {
        return userStatisticsRepo.findAll();
    }
}