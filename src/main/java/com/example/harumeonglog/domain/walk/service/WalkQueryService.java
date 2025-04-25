package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;

public interface WalkQueryService {
    WalkResponse.WalkAvailablePetListResponse getAvailablePets(Member member);
    WalkResponse.WalkAvailableMemberListResponse getAvailableMembers(WalkRequest.AvailableMemberRequest dto);
    WalkResponse.WalkSearchListResponse getWalkList(String sort, Long cursor, int offset);
    WalkResponse.WalkDetailResponse getWalkDetails(Long walkId);
}
