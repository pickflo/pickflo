package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;

@Repository
public interface HomeRecMovieRepository extends JpaRepository<Movie, Long>, SearchQuerydsl {
	
	@Query(value = """
	        WITH MovieGenres AS (
	            SELECT 
	                m.movie_id,
	                m.movie_title,
	                m.movie_img,
	                m.movie_release_date,
	                GROUP_CONCAT(g.genre_name ORDER BY g.genre_name SEPARATOR ', ') AS genres
	            FROM movies m
	            JOIN movies_genres mg ON m.movie_id = mg.movie_id
	            JOIN genres g ON mg.genre_id = g.genre_id
	            GROUP BY m.movie_id, m.movie_title, m.movie_img, m.movie_release_date
	        ),
	        
	        UserMovieGenres AS (
	            SELECT 
	                m.movie_id,
	                m.movie_title,
	                m.movie_img,  
	                GROUP_CONCAT(g.genre_name ORDER BY g.genre_name SEPARATOR ', ') AS genres
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
	                GROUP_CONCAT(g1.genre_name ORDER BY g1.genre_name SEPARATOR ', ') AS overlap_genres,
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
	                mg.movie_release_date,
	                mg.genres,
	                LENGTH(mg.genres) - LENGTH(REPLACE(mg.genres, ',', '')) + 1 AS genre_count
	            FROM MovieGenres mg
	            JOIN GenreOverlap go ON mg.genres = go.overlap_genres
	        )
	        
	        SELECT DISTINCT 
	            m.movie_id,
	            m.movie_title,
	            m.movie_img,
	            m.movie_release_date,
	            m.genres,
	            m.genre_count
	        FROM MatchingMovies m
	        ORDER BY m.genre_count DESC, m.movie_release_date DESC
	        LIMIT :startRow, :pageSize
	    """, 
	    nativeQuery = true)
	List<Object[]> findMoviesByUserId(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize);
}	
	/*
	@Query(value = """
		    WITH MovieGenres AS (
		        SELECT 
		            m.movie_id,
		            m.movie_title,
		            m.movie_img,
		            m.movie_release_date,
		            LISTAGG(g.genre_name, ', ') WITHIN GROUP (ORDER BY g.genre_name) AS genres
		        FROM movies m
		        JOIN movies_genres mg ON m.movie_id = mg.movie_id
		        JOIN genres g ON mg.genre_id = g.genre_id
		        GROUP BY m.movie_id, m.movie_title, m.movie_img, m.movie_release_date
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
		            mg.movie_release_date,
		            mg.genres,
		            LENGTH(mg.genres) - LENGTH(REPLACE(mg.genres, ',', '')) + 1 AS genre_count
		        FROM MovieGenres mg
		        JOIN GenreOverlap go ON mg.genres = go.overlap_genres
		    )
		    
		    SELECT DISTINCT 
		        m.movie_id,
		        m.movie_title,
		        m.movie_img,
		        m.movie_release_date,
		        m.genres,
		        m.genre_count
		    FROM MatchingMovies m
		    ORDER BY m.genre_count DESC, m.movie_release_date DESC
		    OFFSET :startRow ROWS
		    FETCH NEXT :endRow - :startRow + 1 ROWS ONLY
		    """, 
		    nativeQuery = true)
		List<Object[]> findMoviesByUserId(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("endRow") int endRow);
}
*/

	/*
	@Query(value = """
	        SELECT * FROM (
	            SELECT inner_query.*, ROWNUM rnum FROM (
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
	            ) inner_query
	            WHERE ROWNUM <= :endRow
	        )
	        WHERE rnum > :startRow
	        """, 
	    nativeQuery = true)
	List<Object[]> findMoviesByUserId(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("endRow") int endRow);

}
*/

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
=======
package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.Movie;

@Repository
public interface HomeRecMovieRepository extends JpaRepository<Movie, Long>, SearchQuerydsl {
	
	@Query(value = """
	        WITH MovieGenres AS (
	            SELECT 
	                m.movie_id,
	                m.movie_title,
	                m.movie_img,
	                m.movie_release_date,
	                GROUP_CONCAT(g.genre_name ORDER BY g.genre_name SEPARATOR ', ') AS genres
	            FROM movies m
	            JOIN movies_genres mg ON m.movie_id = mg.movie_id
	            JOIN genres g ON mg.genre_id = g.genre_id
	            GROUP BY m.movie_id, m.movie_title, m.movie_img, m.movie_release_date
	        ),
	        
	        UserMovieGenres AS (
	            SELECT 
	                m.movie_id,
	                m.movie_title,
	                m.movie_img,  
	                GROUP_CONCAT(g.genre_name ORDER BY g.genre_name SEPARATOR ', ') AS genres
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
	                GROUP_CONCAT(g1.genre_name ORDER BY g1.genre_name SEPARATOR ', ') AS overlap_genres,
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
	                mg.movie_release_date,
	                mg.genres,
	                LENGTH(mg.genres) - LENGTH(REPLACE(mg.genres, ',', '')) + 1 AS genre_count
	            FROM MovieGenres mg
	            JOIN GenreOverlap go ON mg.genres = go.overlap_genres
	        )
	        
	        SELECT DISTINCT 
	            m.movie_id,
	            m.movie_title,
	            m.movie_img,
	            m.movie_release_date,
	            m.genres,
	            m.genre_count
	        FROM MatchingMovies m
	        ORDER BY m.genre_count DESC, m.movie_release_date DESC
	        LIMIT :startRow, :pageSize
	    """, 
	    nativeQuery = true)
	List<Object[]> findMoviesByUserIdAndGenres(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize);

	@Query(value = """
	        WITH UserWatchedPeople AS (
	            SELECT DISTINCT
	                mp.person_id,
	                mp.movie_person_job
	            FROM users_movies um
	            JOIN movies_people mp ON um.movie_id = mp.movie_id
	            WHERE um.user_id = :userId
	        ),
	        
	        RecommendedMovies AS (
	            SELECT 
	                m.movie_id,
	                m.movie_title,
	                m.movie_img,
	                m.movie_release_date,
	                m.movie_rating,
	                mp.movie_person_job,
	                GROUP_CONCAT(DISTINCT p.person_name ORDER BY p.person_name SEPARATOR ', ') AS people
	            FROM movies_people mp
	            JOIN movies m ON mp.movie_id = m.movie_id
	            JOIN people p ON mp.person_id = p.person_id
	            JOIN UserWatchedPeople uwp ON mp.person_id = uwp.person_id AND mp.movie_person_job = uwp.movie_person_job
	            WHERE m.movie_id NOT IN (
	                SELECT movie_id 
	                FROM users_movies 
	                WHERE user_id = :userId
	            )
	            GROUP BY m.movie_id, m.movie_title, m.movie_img, m.movie_release_date, m.movie_rating, mp.movie_person_job
	        )
	        
	        SELECT 
	            rm.movie_id,
	            rm.movie_title,
	            rm.movie_img,
	            rm.movie_release_date,
	            rm.movie_rating,
	            rm.movie_person_job,
	            rm.people
	        FROM RecommendedMovies rm
	        ORDER BY rm.movie_rating DESC, rm.movie_release_date DESC
	        LIMIT :startRow, :pageSize
	    """, 
	    nativeQuery = true)
	List<Object[]> findMoviesByUserIdAndPeople(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize);

}




