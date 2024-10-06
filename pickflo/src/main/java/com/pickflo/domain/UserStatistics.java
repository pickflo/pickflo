package com.pickflo.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PRIVATE) @Builder
@Entity
@Table(name = "user_statistics", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"user_group", "stat_date"}) 
	    })
public class UserStatistics {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistics_id")
    private Long statisticsId; // 활동 ID

    @Basic(optional = false)
    @Column(name = "user_group")
    private String userGroup; // 유저 그룹 (A/B)

    @Basic(optional = false)
    @Column(name = "time_spent")
    private int timeSpent; // 사이트 이용 시간

    @Basic(optional = false)
    @Column(name = "scroll_count")
    private int scrollCount; // 스크롤한 횟수 

    @Basic(optional = false)
    @Column(name = "like_count")
    private int likeCount; // 좋아요 클릭 수

    @Basic(optional = false)
    @Column(name = "unlike_count")
    private int unlikeCount; // 좋아요 해제 수

    
    @Basic(optional = false)
    @Column(name = "conversion_rate")
    private double conversionRate; // 전환율((likeCount/visitorCount)X100(%)) 
    
    @Basic(optional = false)
    @Column(name = "visitor_count")
    private int visitorCount; // 방문자 수


    @Basic(optional = false)
    @Column(name = "stat_date")
    private LocalDate statDate; // 기록 날짜

    @Basic(optional = false)
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 데이터 생성 시간

    @Basic(optional = false)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 데이터 수정 시간

    // 데이터 생성 전 현재 시간으로 createAt과 statDate 초기화
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now; 
        this.updatedAt = now;
        this.statDate = now.toLocalDate(); 
        this.visitorCount = 0; 
        this.timeSpent = 0;    
        this.scrollCount = 0;  
        this.likeCount = 0;    
        this.unlikeCount = 0; 
        this.conversionRate = 0.0; 
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(); // 수정 시 현재 시간으로 업데이트
    }
    
    // 전환율 계산 메서드
    public void calculateConversionRate() {
        if (visitorCount > 0) {
            this.conversionRate = ((double) likeCount / visitorCount) * 100;
        } else {
            this.conversionRate = 0.0; // 방문자가 없을 경우 전환율 0으로 설정
        }
    }
    
}
