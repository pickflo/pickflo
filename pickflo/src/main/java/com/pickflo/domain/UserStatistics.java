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
@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter
@ToString @EqualsAndHashCode
public class UserStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userGroup;
	
	private int pageView;
	private int scrollCount;
	private int likeCount;
	private int unlikeCount;
	
	@Column(nullable = false)
	private LocalDate weekStartDate;
	@Column(nullable = false)
	private LocalDate weekEndDate;
    private Timestamp lastUpdated; // 마지막 업데이트 시간
}
