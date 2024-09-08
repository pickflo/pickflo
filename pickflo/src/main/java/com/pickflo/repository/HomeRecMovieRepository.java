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
		        SELECT DISTINCT 
		            mg.movie_id,
		            mg.movie_title,
		            mg.movie_img,
		            mg.genres,
		            LENGTH(mg.genres) - LENGTH(REPLACE(mg.genres, ',', '')) + 1 AS genre_count
		        FROM MovieGenres mg
		        JOIN GenreOverlap go ON mg.genres = go.overlap_genres
		    ),
		    
		    RankedMovies AS (
		        SELECT 
		            m.movie_id,
		            m.movie_title,
		            m.movie_img,
		            m.genres,
		            m.genre_count,
		            ROW_NUMBER() OVER (ORDER BY m.genre_count DESC, m.movie_title) AS row_num
		        FROM MatchingMovies m
		    )

		    SELECT 
		        rm.movie_id,
		        rm.movie_title,
		        rm.movie_img,
		        rm.genres,
		        rm.genre_count
		    FROM RankedMovies rm
		    WHERE rm.row_num BETWEEN :startRow AND :endRow
		    """, 
		    nativeQuery = true)
		List<Object[]> findMoviesByUserId(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("endRow") int endRow);

		/*
		 * @Query(value = """
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

		 * 
		 */
}