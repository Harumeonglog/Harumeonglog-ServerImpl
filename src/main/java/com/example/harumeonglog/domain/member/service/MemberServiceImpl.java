package com.example.harumeonglog.domain.member.service;

import com.example.harumeonglog.domain.member.dto.request.MemberRequest;
import com.example.harumeonglog.domain.member.entity.Member;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Override
    public Member updateInfo(Member member, MemberRequest.MemberInfoUpdateRequest request) {
        return null;
    }
}
