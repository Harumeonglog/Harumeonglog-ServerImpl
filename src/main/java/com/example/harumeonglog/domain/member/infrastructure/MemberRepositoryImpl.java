package com.example.harumeonglog.domain.member.infrastructure;

import com.example.harumeonglog.domain.member.entity.MemberEntity;
import com.example.harumeonglog.domain.member.service.port.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

    @Override
    public Optional<MemberEntity> findByEmail(String email) {
        return null;
    }
}
