package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pickflo.domain.UserStatistics;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long>{
	UserStatistics findByUserGroup(String userGroup);
}
