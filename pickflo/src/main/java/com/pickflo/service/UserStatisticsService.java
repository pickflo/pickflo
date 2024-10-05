package com.pickflo.service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pickflo.domain.UserStatistics;
import com.pickflo.dto.ChartDto;
import com.pickflo.repository.UserStatisticsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatisticsService {

    private final UserStatisticsRepository repo;
    
    public LocalDate getWeekStartDate(int weekOffset, LocalDate specificDate) {
        LocalDate weekStartDate;

        // 특정 날짜를 기반으로 주 시작일 계산
        if (weekOffset == -1) {
            // 저번주
            weekStartDate = specificDate.with(DayOfWeek.MONDAY).minusWeeks(1);
        } else if (weekOffset == 1) {
            // 다음주
            weekStartDate = specificDate.with(DayOfWeek.MONDAY).plusWeeks(1);
        } else {
            // 이번주
            weekStartDate = specificDate.with(DayOfWeek.MONDAY);
        }

        return weekStartDate;
    }

    public void saveUserData(ChartDto userData) {
        // 현재 날짜
        LocalDate date = LocalDate.now();
        
        // 현재 주의 시작 날짜(월요일)
        //LocalDate weekStartDate = today.with(java.time.DayOfWeek.MONDAY);
        
        //LocalDate testDate = LocalDate.of(2024, 10, 1); // 특정 테스트 날짜 설정
        //LocalDate previousDate = getWeekStartDate(-1, weekStartDate); // 저번주 시작일
        //LocalDate startDateNext = getWeekStartDate(1, weekStartDate); // 다음주 시작일
        
      
        
        // 사용자 그룹에 따른 통계 가져오기
        UserStatistics userStatistics = repo.findByUserGroupAndDate(userData.getUserGroup(), date);

        if (userStatistics == null) {
            // 새로운 통계 생성
            userStatistics = UserStatistics.builder()
                    .userGroup(userData.getUserGroup())
                    .pageView(0)
                    .scrollCount(0)
                    .likeCount(0)
                    .unlikeCount(0)
                    .totalTimeSpent(0) 
                    .actionCompleted("N")
                    .date(date)
                    .lastUpdated(new Timestamp(System.currentTimeMillis())) // 마지막 업데이트 시간도 초기화
                    .build();
        }

        // actionType에 따라 통계 증가
        switch (userData.getActionType()) {
            case "page_view":
                userStatistics.setPageView(userStatistics.getPageView() + 1);
                break;
            case "scroll":
                userStatistics.setScrollCount(userStatistics.getScrollCount() + 1);
                break;
            case "like":
                userStatistics.setLikeCount(userStatistics.getLikeCount() + 1);
                break;
            case "unlike":
                userStatistics.setUnlikeCount(userStatistics.getUnlikeCount() + 1);
                break;
        }

        // 마지막 업데이트 시간 갱신
        userStatistics.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        
        // DB에 저장
        repo.save(userStatistics);
    }
/*
    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 자정에 실행
    public void createWeeklyStatistics() {
        // A그룹과 B그룹 통계 확인 및 새로운 행 생성
        createGroupStatistics("A");
        createGroupStatistics("B");
    }
*/
    private void createGroupStatistics(String group) {
       // LocalDate weekStartDate = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
       // LocalDate weekEndDate = weekStartDate.plusDays(6);

       LocalDate date = LocalDate.now();
       

        // 이미 해당 주의 통계가 존재하는지 확인
        if (repo.findByUserGroupAndDate(group, date) == null) {
            UserStatistics newStatistics = UserStatistics.builder()
                    .userGroup(group)
                    .pageView(0)
                    .scrollCount(0)
                    .likeCount(0)
                    .unlikeCount(0)
                    .totalTimeSpent(0) 
                    .actionCompleted("N")
                    .date(date)
                    .lastUpdated(new Timestamp(System.currentTimeMillis())) // 마지막 업데이트 시간도 초기화
                    .build();
            repo.save(newStatistics);
        }
    }
    
    public List<UserStatistics> getUserStatisticsByDate(LocalDate date) {
        // 사용자가 지정한 날짜의 통계 가져오기
        List<UserStatistics> userStatistics = repo.findByDate(date);
        return userStatistics;
    }
    
    /*
    public List<UserStatistics> getUserStatisticsByWeekStart(int weekOffset) {
        // 현재 날짜
        LocalDate today = LocalDate.now();
        LocalDate weekStartDate = getWeekStartDate(weekOffset, today);

        // 사용자 그룹에 따른 통계 가져오기
        List<UserStatistics> userStatistics = repo.findByDate(weekStartDate);

        return userStatistics;
    }
*/

    
}
