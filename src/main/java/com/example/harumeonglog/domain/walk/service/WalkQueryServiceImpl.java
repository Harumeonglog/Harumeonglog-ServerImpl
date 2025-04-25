package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalkQueryServiceImpl implements WalkQueryService {

    private final MemberPetRepository memberPetRepository;

    @Override
    public WalkResponse.WalkAvailablePetListResponse getAvailablePets(Member member) {
        return null;
    }

    @Override
    public WalkResponse.WalkAvailableMemberListResponse getAvailbleMembers(WalkRequest.AvailableMemberRequest dto) {
        return null;
    }

    @Override
    public WalkResponse.WalkSearchListResponse getWalkList(String sort, Long cursor, int offset) {
        return null;
    }

    @Override
    public WalkResponse.WalkDetailResponse getWalkDetails(Long walkId) {
        return null;
    }
}
