package com.pickflo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "movies_people")
@IdClass(MoviePersonId.class)
public class MoviePerson {
	
	@Id
	@Column(name = "movie_id")
	private Long movieId;
	
	@Id
	@Column(name = "movie_person_job")
	private String moviePersonJob;
	   
	@Id
	@Column(name = "person_id", nullable = false)
	private Long personId;
	   
	@ManyToOne
	@JoinColumn(name = "movie_id", insertable = false, updatable = false)
	private Movie movie;
	
	@ManyToOne
	@JoinColumn(name = "person_id", insertable = false, updatable = false)
	private Person person;
	
	
	
	
	
	
}
