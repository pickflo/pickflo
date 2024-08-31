package com.pickflo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickflo.domain.UserMoviePick;
import com.pickflo.domain.UserMoviePickId;
import com.pickflo.dto.UserMovieLikeDto;
import java.util.List;

@Repository
public interface UserMovieLikeRepository extends JpaRepository<UserMoviePick, UserMoviePickId> {

	//유저의 찜한 영화리스트
    @Query("SELECT new com.pickflo.dto.UserMovieLikeDto(ump.user.id, m.id, m.movieImg) " +
           "FROM UserMoviePick ump JOIN ump.movie m WHERE ump.user.id = :userId")
    List<UserMovieLikeDto> findByUserId(@Param("userId") Long userId);
}
