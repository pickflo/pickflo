package com.pickflo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;

@Repository
public interface HomeRecMovieRepository extends JpaRepository<Movie, Long>, SearchQuerydsl {
	/*
	@Query(value = """
	        WITH AllMovies AS (
	            SELECT 
	                m.movie_id,
	                m.movie_title,
	                m.movie_img,
	                LISTAGG(g.genre_name, ', ') WITHIN GROUP (ORDER BY g.genre_name) AS genres
	            FROM movies m
	            JOIN movies_genres mg ON m.movie_id = mg.movie_id
	            JOIN genres g ON mg.genre_id = g.genre_id
	            GROUP BY m.movie_id, m.movie_title, m.movie_img
	        ),

	        UserMoviesGenres AS (
	            SELECT 
	                m.movie_id,
	                m.movie_title,
	                m.movie_img,
	                LISTAGG(g.genre_name, ', ') WITHIN GROUP (ORDER BY g.genre_name) AS genres
	            FROM users_movies um
	            JOIN movies m ON m.movie_id = um.movie_id
	            JOIN movies_genres mg ON um.movie_id = mg.movie_id
	            JOIN genres g ON mg.genre_id = g.genre_id
	            WHERE um.user_id = :userId
	            GROUP BY m.movie_id, m.movie_title, m.movie_img
	        )

	        SELECT 
	            am.movie_id AS all_movie_id,
	            am.movie_title AS all_movie_title,
	            am.movie_img AS all_movie_img,
	            am.genres AS all_movie_genres
	        FROM AllMovies am
	        JOIN UserMoviesGenres um ON am.genres = um.genres
	        WHERE am.movie_id != um.movie_id
	        ORDER BY am.genres, am.movie_title
	    """, nativeQuery = true)
		List<Object[]> findMoviesByUserId(@Param("userId") Long userId);
}
*/
	
	
	@Query(value = """
		    WITH MovieGenres AS (
		        SELECT 
		            m.movie_id,
		            m.movie_title,
		            m.movie_img,  
		            LISTAGG(g.genre_name, ', ') WITHIN GROUP (ORDER BY g.genre_name) AS genres
		        FROM movies m
		        JOIN movies_genres mg ON m.movie_id = mg.movie_id
		        JOIN genres g ON mg.genre_id = g.genre_id
		        GROUP BY m.movie_id, m.movie_title, m.movie_img
		    ),
		    
		    UserMovieGenres AS (
		        SELECT 
		            m.movie_id,
		            m.movie_title,
		            m.movie_img,  
		            LISTAGG(g.genre_name, ', ') WITHIN GROUP (ORDER BY g.genre_name) AS genres
		        FROM users_movies um
		        JOIN movies m ON m.movie_id = um.movie_id
		        JOIN movies_genres mg ON m.movie_id = mg.movie_id
		        JOIN genres g ON mg.genre_id = g.genre_id
		        WHERE um.user_id = :userId
		        GROUP BY m.movie_id, m.movie_title, m.movie_img
		    ),
		    
		    GenreOverlap AS (
		        SELECT 
		            a.movie_id AS movie_id_1,
		            b.movie_id AS movie_id_2,
		            LISTAGG(g1.genre_name, ', ') WITHIN GROUP (ORDER BY g1.genre_name) AS overlap_genres,
		            COUNT(DISTINCT g1.genre_name) AS overlap_count
		        FROM UserMovieGenres a
		        JOIN UserMovieGenres b ON a.movie_id < b.movie_id
		        JOIN movies_genres mg1 ON a.movie_id = mg1.movie_id
		        JOIN genres g1 ON mg1.genre_id = g1.genre_id
		        JOIN movies_genres mg2 ON b.movie_id = mg2.movie_id
		        JOIN genres g2 ON mg2.genre_id = g2.genre_id
		        WHERE g1.genre_name = g2.genre_name
		        GROUP BY a.movie_id, b.movie_id
		    ),
		    
		    MatchingMovies AS (
		        SELECT 
		            mg.movie_id,
		            mg.movie_title,
		            mg.movie_img,
		            mg.genres,
		            LENGTH(mg.genres) - LENGTH(REPLACE(mg.genres, ',', '')) + 1 AS genre_count
		        FROM MovieGenres mg
		        JOIN GenreOverlap go ON mg.genres = go.overlap_genres
		    )
		    
		    SELECT DISTINCT 
		        m.movie_id,
		        m.movie_title,
		        m.movie_img,
		        m.genres,
		        m.genre_count
		    FROM MatchingMovies m
		    ORDER BY m.genre_count DESC, m.movie_title
		    """, nativeQuery = true)
	List<Object[]> findMoviesByUserId(@Param("userId") Long userId);
}


/*
@Query(value = 
	    "WITH rankeditems AS ( " +
	    "    SELECT " +
	    "        um.user_id, " +
	    "        '장르' AS 타입, " +
	    "        g.genre_name AS 이름, " +
	    "        SUM(COALESCE(i.importance_value, 0)) AS 선호도 " +
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
	    "        SUM(COALESCE(i.importance_value, 0)) AS 선호도 " +
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
	    "        SUM(COALESCE(i.importance_value, 0)) AS 선호도 " +
	    "    FROM movies m " +
	    "    JOIN users_movies um ON m.movie_id = um.movie_id " +
	    "    JOIN movies_people mp ON m.movie_id = mp.movie_id " +
	    "    JOIN people p ON mp.person_id = p.person_id " +
	    "    JOIN importance i ON i.importance_type = '사람' " +
	    "    WHERE um.user_id = :userId " +
	    "    GROUP BY um.user_id, p.person_name " +
	    "), " +
	    "all_movies AS ( " +
	    "    SELECT " +
	    "        m.movie_id, " +
	    "        m.movie_img " +
	    "    FROM movies m " +
	    "    JOIN movies_genres mg ON m.movie_id = mg.movie_id " +
	    "    JOIN genres g ON mg.genre_id = g.genre_id " +
	    "    UNION all " +
	    "    SELECT distinct " +
	    "        m.movie_id, " +
	    "        m.movie_img " +
	    "    FROM movies m " +
	    "    JOIN movies_countries mc ON m.movie_id = mc.movie_id " +
	    "    JOIN countries c ON mc.country_id = c.country_id " +
	    "    WHERE NOT EXISTS ( " +
	    "        SELECT 1 " +
	    "        FROM movies_genres mg " +
	    "        WHERE mg.movie_id = m.movie_id " +
	    "    ) " +
	    "    UNION all " +
	    "    SELECT distinct " +
	    "        m.movie_id, " +
	    "        m.movie_img " +
	    "    FROM movies m " +
	    "    JOIN movies_people mp ON m.movie_id = mp.movie_id " +
	    "    JOIN people p ON mp.person_id = p.person_id " +
	    "    WHERE NOT EXISTS ( " +
	    "        SELECT 1 " +
	    "        FROM movies_genres mg " +
	    "        WHERE mg.movie_id = m.movie_id " +
	    "    ) " +
	    "    AND NOT EXISTS ( " +
	    "        SELECT 1 " +
	    "        FROM movies_countries mc " +
	    "        WHERE mc.movie_id = m.movie_id " +
	    "    ) " +
	    ") " +
	    "SELECT distinct " +
	    "    movie_id, " +
	    "    movie_img " +
	    "FROM all_movies",
	    nativeQuery = true)

List<Object[]> findMoviesByUserId(@Param("userId") Long userId);

} */
