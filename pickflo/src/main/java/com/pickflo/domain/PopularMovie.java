package com.pickflo.domain;

import jakarta.persistence.Basic;
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
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Getter @Builder
@ToString @EqualsAndHashCode
@Entity
@Table(name="SURVEY_MOVIES")
public class PopularMovie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Basic(optional = false) // not null 
	private Long code;
	
	@Basic(optional = false) // not null 
	private String title;
	
	@Basic(optional = false) // not null 
	private String img;
}
