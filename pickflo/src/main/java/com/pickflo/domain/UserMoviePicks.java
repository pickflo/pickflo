package com.pickflo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter @Builder
@ToString @EqualsAndHashCode
@Entity
@Table(name="USER_MOVIES")
public class UserMoviePicks {
	
	@Id
	private Long userId;
	
	@Id
	private Long movieId;
}
