package com.pickflo.service;

import com.pickflo.domain.User;
import com.pickflo.domain.UserStatistics;
import com.pickflo.domain.UserVisit;
import com.pickflo.repository.UserRepository;
import com.pickflo.repository.UserStatisticsRepository;
import com.pickflo.repository.UserVisitRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final UserStatisticsRepository userStatisticsRepo;
    private final UserVisitRepository userVisitRepo;
    private final UserRepository userRepo;
    
    @Transactional
    public void incrementVisitCount(Long userId) {
        // 사용자 ID에 따라 유저 그룹을 결정
        String userGroup = (userId % 2 == 0) ? "B" : "A";

        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 오늘 해당 유저 그룹에 대한 통계 항목 조회
        UserStatistics statistics = userStatisticsRepo.findByUserGroupAndStatDate(userGroup, today)
                .stream().findFirst().orElseGet(() -> createNewStatistics(userGroup, today));
      
        statistics.setVisitorCount(statistics.getVisitorCount() + 1); // 방문자 수 증가
        statistics.setUpdatedAt(LocalDateTime.now());
        
        saveUserVisit(userId);
        
        calculateRevisitRate(today); // 재방문율 계산
        
        // 변경 사항 저장
        userStatisticsRepo.save(statistics);
    }
    

    private void saveUserVisit(Long userId) {
        
        Optional<User> userOptional = userRepo.findById(userId);
               
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        UserVisit userVisit = UserVisit.builder()
                .user(user) 
                .visitDate(LocalDateTime.now()) 
                .build();
        
        userVisitRepo.save(userVisit);
    }
    
    @Transactional
    public void saveUserData(UserStatistics userData) {
        LocalDate today = LocalDate.now();
        UserStatistics statistics = userStatisticsRepo.findByUserGroupAndStatDate(userData.getUserGroup(), today)
                .stream().findFirst().orElseGet(() -> createNewStatistics(userData.getUserGroup(), today));

        statistics.setTimeSpent(statistics.getTimeSpent() + userData.getTimeSpent());
        statistics.setScrollCount(statistics.getScrollCount() + userData.getScrollCount());
        statistics.setLikeCount(statistics.getLikeCount() + userData.getLikeCount());
        statistics.setUnlikeCount(statistics.getUnlikeCount() + userData.getUnlikeCount());
        
        calculateConversionRate(statistics); // 전환율 계산

        statistics.setUpdatedAt(LocalDateTime.now()); // updatedAt 업데이트
        
        userStatisticsRepo.save(statistics); // 변경 사항 저장
    }
    
    public void calculateRevisitRate(LocalDate today) {
        // 사용자 방문 데이터 조회
        List<Long> distinctUserIds = userVisitRepo.findDistinctUserIdByVisitDateBetween(today.atStartOfDay(), today.atTime(23, 59, 59));
        
        int totalVisitorsA = 0;
        int revisitCountA = 0;
        int totalVisitorsB = 0;
        int revisitCountB = 0;

        // 각 그룹별로 방문자와 재방문자 수 계산
        for (Long userId : distinctUserIds) {
            String userGroup = (userId % 2 == 0) ? "B" : "A";

            if ("A".equals(userGroup)) {
                totalVisitorsA++;
                // A 그룹에서 재방문자 수를 세는 로직
                if (userVisitRepo.countByUserIdAndVisitDateAfter(userId, LocalDateTime.now().minusDays(1)) > 1) {
                    revisitCountA++;
                }
            } else { // B 그룹
                totalVisitorsB++;
                // B 그룹에서 재방문자 수를 세는 로직
                if (userVisitRepo.countByUserIdAndVisitDateAfter(userId, LocalDateTime.now().minusDays(1)) > 1) {
                    revisitCountB++;
                }
            }
        }

        // 재방문율 계산
        double revisitRateA = (totalVisitorsA > 0) ? ((double) revisitCountA / totalVisitorsA) * 100 : 0.0;
        double revisitRateB = (totalVisitorsB > 0) ? ((double) revisitCountB / totalVisitorsB) * 100 : 0.0;

        // 통계 업데이트
        updateRevisitRate("A", revisitRateA, today);
        updateRevisitRate("B", revisitRateB, today);
    }

    
    private void updateRevisitRate(String userGroup, double revisitRate, LocalDate today) {
        UserStatistics statistics = userStatisticsRepo.findByUserGroupAndStatDate(userGroup, today)
                .stream().findFirst().orElse(null);

        if (statistics != null) {
            statistics.setRevisitRate(revisitRate);
            userStatisticsRepo.save(statistics);
        }
    }

    
    // 전환율 계산
    private void calculateConversionRate(UserStatistics statistics) {
        if (statistics.getVisitorCount() > 0) {
            statistics.setConversionRate(((double) statistics.getLikeCount() / statistics.getVisitorCount()) * 100);
        } else {
            statistics.setConversionRate(0.0); // 방문자가 없을 경우 전환율 0으로 설정
        }
    }
    
    private UserStatistics createNewStatistics(String userGroup, LocalDate statDate) {
        return UserStatistics.builder()
                .userGroup(userGroup)
                .timeSpent(0)
                .scrollCount(0)
                .likeCount(0)
                .unlikeCount(0)
                .visitorCount(0)
                .conversionRate(0.0)
                .revisitRate(0.0)
                .statDate(statDate)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public List<UserStatistics> getUserStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        return userStatisticsRepo.findByStatDateBetween(startDate, endDate);
    }
    
}