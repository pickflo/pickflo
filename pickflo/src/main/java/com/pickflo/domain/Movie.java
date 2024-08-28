package com.pickflo.domain;
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
	@Column(name = "MOVIES_ID")
	private Long id;
	
	@Basic(optional = false)
	private String movieTitle;
	
	@Basic(optional = false)
	private String movieOverview;
	
	@Basic(optional = false)
	private String movieRating;
	
	@Basic(optional = false)
	private String movieReleaseDate;
	
	@Basic(optional = false)
	private String movieRuntime;
	
	private String movieImg;
	
}
