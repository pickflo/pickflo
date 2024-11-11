package com.pickflo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserMovieLikeDto {

	private Long userId;
	private Long movieId;
	private String movieImg;
}
