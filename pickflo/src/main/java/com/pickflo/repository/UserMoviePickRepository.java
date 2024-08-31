package com.pickflo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.pickflo.domain.UserMoviePick;
import com.pickflo.domain.UserMoviePickId;

public interface UserMoviePickRepository extends JpaRepository<UserMoviePick, UserMoviePickId> {

    // 사용자가 찜한 항목의 갯수를 카운트하는 쿼리
    @Query("select count(ump) from UserMoviePick ump where ump.user.id = :userId")
    int countByUserId(@Param("userId") Long userId);
    
}
