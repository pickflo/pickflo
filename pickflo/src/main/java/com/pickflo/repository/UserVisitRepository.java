package com.pickflo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.UserVisit;


@Repository
public interface UserVisitRepository extends JpaRepository<UserVisit, Long> {
	
	@Query("SELECT DISTINCT u.user.id FROM UserVisit u WHERE u.visitDate BETWEEN ?1 AND ?2")
    List<Long> findDistinctUserIdByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    long countByUserIdAndVisitDateBefore(Long userId, LocalDateTime date);
    
    // 추가된 메소드: 특정 사용자 ID에 대한 방문 수를 세기 위해 재방문자 수를 세는 메소드
    @Query("SELECT COUNT(u) FROM UserVisit u WHERE u.user.id = :userId AND u.visitDate >= :visitDate")
    long countByUserIdAndVisitDateAfter(@Param("userId") Long userId, @Param("visitDate") LocalDateTime visitDate);
}