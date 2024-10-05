package com.pickflo.domain;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_statistics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String userGroup;

	private int pageView;
	private int scrollCount;
	private int likeCount;
	private int unlikeCount;
	private int totalTimeSpent;
	private String actionCompleted; // 행동 완료 여부 (Y 또는 N)

	@Column(nullable = false)
	private LocalDate date;

	private Timestamp lastUpdated; // 마지막 업데이트 시간
}
