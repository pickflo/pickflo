package com.pickflo.domain;
import java.sql.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "MOVIES")
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MOVIE_ID")
	private Long id;
	
	@Column(unique = true)
	@Basic(optional = false)
	private Long movieCode;
	
	@Basic(optional = false)
	private String movieTitle;
	
	@Basic(optional = false)
	private String movieOverview;
	
	@Basic(optional = false)
	private Double movieRating;
	
	@Basic(optional = false)
	private Date movieReleaseDate;
	
	@Basic(optional = false)
	private Integer movieRuntime;
	
	private String movieImg;
	
}
