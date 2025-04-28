package com.example.harumeonglog.domain.walk.repository;

import com.example.harumeonglog.domain.walk.entity.MemberWalk;
import com.example.harumeonglog.domain.walk.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWalkRepository extends JpaRepository<MemberWalk, Long> {
    MemberWalk findTopByWalkOrderByCreatedAtAsc(Walk walk);
}
