package com.pickflo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchGenreDto {
	private Long movieCode;
	private String movieImg;
}
