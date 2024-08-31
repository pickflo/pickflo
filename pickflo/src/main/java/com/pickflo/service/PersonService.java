package com.pickflo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickflo.domain.Movie;
import com.pickflo.domain.MoviePerson;
import com.pickflo.domain.Person;
import com.pickflo.repository.MovieClient;
import com.pickflo.repository.MoviePersonRepository;
import com.pickflo.repository.MovieClient.MovieCreditsResponse;
import com.pickflo.repository.MovieClient.PersonData;
import com.pickflo.repository.MovieRepository;
import com.pickflo.repository.PersonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonService {

	private final MovieClient movieClient;
	private final MovieRepository movieRepo;
	private final PersonRepository personRepo;
	private final MoviePersonRepository moviePersonRepo;

	@Value("${tmdb.api.key}")
	private String apiKey;

	@Value("${tmdb.language}")
	private String language;
	
	private static final int MAX_CAST = 10;
	private static final int MAX_CREW = 2;

	@Transactional
	public void savePersonByMovieId(Long id) {
		Movie movie = movieRepo.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));

		MovieCreditsResponse response = movieClient.getMoviePerson(apiKey, movie.getMovieCode(), language);
		List<PersonData> castList = response.getCast(); // api로부터 배우 저장
		List<PersonData> crewList = response.getCrew(); // api로부터 감독 저장

		savePersons(id, castList, "Acting", MAX_CAST);
		savePersons(id, crewList, "Directing", MAX_CREW);
	}

	private void savePersons(Long movieId, List<PersonData> personDataList, String department, int max) {
		int count=0;
        for (PersonData data : personDataList) {
            if (department.equalsIgnoreCase(data.getKnown_for_department())) {
                // Person 엔티티 조회, 데이터 없으면 저장
                Person person = personRepo.findByPersonName(data.getName()).orElseGet(() -> {
                    Person newPerson = Person.builder().personName(data.getName()).build();
                    return personRepo.save(newPerson);
                });

                // MoviePerson 엔티티 db 저장
                MoviePerson moviePerson = MoviePerson
                        .builder()
                        .movieId(movieId).personId(person.getId()).job(department)
                        .movie(movieRepo.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found")))
                        .person(person)
                        .build();
                moviePersonRepo.save(moviePerson);

                count++;
                if (count >= max) {
                    break; 
                }
            }
        }
    }
}