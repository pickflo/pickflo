package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pickflo.domain.Movie;
import com.pickflo.dto.HomeRecMovieDto;

public interface HomeRecMovieRepository extends JpaRepository<Movie, Long>, SearchQuerydsl {

	@Query(value = 
            "WITH rankeditems AS ( " +
            "    SELECT " +
            "        um.user_id, " +
            "        '장르' AS 타입, " +
            "        g.genre_name AS 이름, " +
            "        SUM(NVL(i.importance_value, 0)) AS 선호도, " +
            "        ROW_NUMBER() OVER (PARTITION BY '장르' ORDER BY SUM(nvl(i.importance_value, 0)) DESC) AS rn " +
            "    FROM movies m " +
            "    JOIN users_movies um ON m.movie_id = um.movie_id " +
            "    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
            "    JOIN genres g ON mg.genre_id = g.genre_id " +
            "    JOIN importance i ON i.importance_type = '장르' " +
            "    WHERE um.user_id = :userId " +
            "    GROUP BY um.user_id, g.genre_name " +
            "    UNION all " +
            "    SELECT " +
            "        um.user_id, " +
            "        '국가' AS 타입, " +
            "        c.country_name AS 이름, " +
            "        SUM(NVL(i.importance_value, 0)) AS 선호도, " +
            "        ROW_NUMBER() OVER (PARTITION BY '국가' ORDER BY SUM(nvl(i.importance_value, 0)) DESC) AS rn " +
            "    FROM movies m " +
            "    JOIN users_movies um ON m.movie_id = um.movie_id " +
            "    JOIN movies_countries mc ON m.movie_id = mc.movie_id " +
            "    JOIN countries c ON mc.country_id = c.country_id " +
            "    JOIN importance i ON i.importance_type = '국가' " +
            "    WHERE um.user_id = :userId " +
            "    GROUP BY um.user_id, c.country_name " +
            "    UNION all " +
            "    SELECT " +
            "        um.user_id, " +
            "        '사람' AS 타입, " +
            "        p.person_name AS 이름, " +
            "        SUM(NVL(i.importance_value, 0)) AS 선호도, " +
            "        ROW_NUMBER() OVER (PARTITION BY '사람' ORDER BY SUM(nvl(i.importance_value, 0)) DESC) AS rn " +
            "    FROM movies m " +
            "    JOIN users_movies um ON m.movie_id = um.movie_id " +
            "    JOIN movies_people mp ON m.movie_id = mp.movie_id " +
            "    JOIN people p ON mp.person_id = p.person_id " +
            "    JOIN importance i ON i.importance_type = '사람' " +
            "    WHERE um.user_id = :userId " +
            "    GROUP BY um.user_id, p.person_name " +
            "), topitems AS ( " +
            "    SELECT " +
            "        타입, " +
            "        이름 " +
            "    FROM rankeditems " +
            "    WHERE rn <= 2 " +
            "), " +
            "all_movies AS ( " +
            "    SELECT " +
            "        m.movie_id, " +
            "        m.movie_img " +
            
            "    FROM movies m " +
            "    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
            "    JOIN genres g ON mg.genre_id = g.genre_id " +
            "    JOIN topitems ti ON ti.타입 = '장르' AND ti.이름 = g.genre_name " +
            "    UNION all " +
            "    SELECT " +
            "        m.movie_id, " +
            "        m.movie_img " +
            
            "    FROM movies m " +
            "    JOIN movies_countries mc ON m.movie_id = mc.movie_id " +
            "    JOIN countries c ON mc.country_id = c.country_id " +
            "    JOIN topitems ti ON ti.타입 = '국가' AND ti.이름 = c.country_name " +
            "    UNION all " +
            "    SELECT " +
            "        m.movie_id, " +
            "        m.movie_img " +
            
            "    FROM movies m " +
            "    JOIN movies_people mp ON m.movie_id = mp.movie_id " +
            "    JOIN people p ON mp.person_id = p.person_id " +
            "    JOIN topitems ti ON ti.타입 = '사람' AND ti.이름 = p.person_name " +
            ") " +
            "SELECT distinct " +
            "    movie_id, " +
            "    movie_img " +
            
            "FROM all_movies " , 
            
            nativeQuery = true)
    List<Object[]> findMoviesByUserId(@Param("userId") Long userId);
	    
}
