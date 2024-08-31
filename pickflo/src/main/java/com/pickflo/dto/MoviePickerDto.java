package com.pickflo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MoviePickerDto {
	
	private Long id;
	private String title;
	private String img;
}
