package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRepository extends JpaRepository<Walk, Long> {
}
