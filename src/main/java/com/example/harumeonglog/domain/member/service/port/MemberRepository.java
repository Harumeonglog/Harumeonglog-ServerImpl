package com.example.harumeonglog.domain.member.service.port;

import com.example.harumeonglog.domain.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepository {
    Optional<MemberEntity> findByEmail(String email);
}
