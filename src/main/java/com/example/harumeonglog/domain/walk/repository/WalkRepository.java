package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.Walk;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalkRepository extends JpaRepository<Walk, Long> {
    Slice<Walk> findAllByIsSharedIsTrueOrderByDistanceDescIdDesc(Pageable pageable);

    @Query("SELECT w FROM Walk w WHERE " +
            "(w.distance < (SELECT w1.distance FROM  Walk w1 WHERE w1.id = :cursor) OR (w.distance = (SELECT w1.distance FROM  Walk w1 WHERE w1.id = :cursor) AND w.id < :cursor)) " +
            "AND w.isShared = true " +
            "ORDER BY w.distance DESC, w.id DESC")
    Slice<Walk> findAllByDistanceDesc(@Param("cursor") Long cursor, Pageable pageable);

    Slice<Walk> findAllByIsSharedIsTrueOrderByTimeDescIdDesc(Pageable pageable);

    @Query("SELECT w FROM Walk w WHERE " +
            "(w.time < (SELECT w1.time FROM Walk w1 WHERE w1.id = :cursor) OR (w.time = (SELECT w1.time FROM Walk w1 WHERE w1.id = :cursor) AND w.id < :cursor)) " +
            "AND w.isShared = true " +
            "ORDER BY w.time DESC, w.id DESC")
    Slice<Walk> findAllByTimeDesc(@Param("cursor") Long cursor, Pageable pageable);

    Slice<Walk> findAllByIsSharedIsTrueOrderByWalkLikeNumDescIdDesc(Pageable pageable);

    @Query("SELECT w FROM Walk w WHERE " +
            "(w.walkLikeNum < (SELECT w1.walkLikeNum FROM Walk w1 WHERE w1.id = :cursor) OR (w.walkLikeNum = (SELECT w1.walkLikeNum FROM Walk w1 WHERE w1.id = :cursor) AND w.id < :cursor)) " +
            "AND w.isShared = true " +
            "ORDER BY w.walkLikeNum DESC, w.id DESC")
    Slice<Walk> findAllByWalkLikeNumDesc(@Param("cursor") Long cursor, Pageable pageable);
}
