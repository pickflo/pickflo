package com.pickflo.domain;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_statistics")
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter
public class UserStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userGroup;
	private int pageView;
	private int scrollCount;
	private int likeCount;
	private int unlikeCount;
    private Timestamp lastUpdated; // 마지막 업데이트 시간
}
