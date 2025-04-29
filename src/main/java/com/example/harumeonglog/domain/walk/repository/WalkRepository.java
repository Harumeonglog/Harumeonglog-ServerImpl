package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.Walk;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalkRepository extends JpaRepository<Walk, Long> {
    @Query("SELECT w FROM Walk w WHERE " +
            "w.id < :cursor AND w.isShared = true " +
            "ORDER BY w.distance DESC, w.id DESC")
    Slice<Walk> findAllByDistance(@Param("cursor") Long cursor, Pageable pageable);
}
