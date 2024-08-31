package com.pickflo.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MoviePersonId {
	
	private Long movieId;
	private String moviePersonJob;
	private Long personId;
}
