package com.example.harumeonglog.domain.walk.service;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.domain.walk.converter.WalkConverter;
import com.example.harumeonglog.domain.walk.dto.request.WalkRequest;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.Walk;
import com.example.harumeonglog.domain.walk.entity.enums.WalkStatus;
import com.example.harumeonglog.domain.walk.repository.MemberWalkRepository;
import com.example.harumeonglog.domain.walk.repository.WalkLikeRepository;
import com.example.harumeonglog.domain.walk.repository.WalkRepository;
import com.example.harumeonglog.global.error.code.WalkErrorCode;
import com.example.harumeonglog.global.error.exception.WalkException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalkQueryServiceImpl implements WalkQueryService {

    private final WalkRepository walkRepository;
    private final MemberPetRepository memberPetRepository;
    private final WalkLikeRepository walkLikeRepository;
    private final MemberWalkRepository memberWalkRepository;

    @Override
    public WalkResponse.WalkAvailablePetListResponse getAvailablePets(Member member) {
        List<Pet> availablePets = memberPetRepository.findByMember(member.getId()).stream().map(MemberPet::getPet).toList();
        return WalkConverter.toWalkAvailablePetListResponse(availablePets);
    }

    @Override
    public WalkResponse.WalkAvailableMemberListResponse getAvailableMembers(WalkRequest.AvailableMemberRequest dto) {
        Set<Member> members = new HashSet<>();
        dto.getPetId().forEach(petId ->
            members.addAll(memberPetRepository.findByPet(petId).stream().map(MemberPet::getMember).toList())
        );
        return WalkConverter.toWalkAvailableMemberListResponse(members);
    }

    @Override
    public WalkResponse.WalkSearchListResponse getWalkList(Member member, String sort, Long cursor, int offset) {
        Pageable pageable = PageRequest.of(0, offset);

        Slice<Walk> walks;
        if (cursor.equals(0L)) {
            cursor = Long.MAX_VALUE;
        }
        // FIXME: Sort에 따른 쿼리 수정 필요
        walks = walkRepository.findAllByDistance(cursor, pageable);

        return buildWalkSearchListResponse(member, walks);
    }

    @Override
    public WalkResponse.WalkDetailResponse getWalkDetails(Member member, Long walkId) {
        Walk walk = findById(walkId);

        return WalkConverter.toWalkDetailResponse(walk, memberWalkRepository.findTopByWalkOrderByCreatedAtAsc(walk).getMember().getNickname(), walkLikeRepository.existsByMemberAndWalk(member, walk));
    }

    @Override
    public Walk findById(Long walkId) {
        return walkRepository.findById(walkId).orElseThrow(() -> new WalkException(WalkErrorCode.NOT_FOUND));
    }

    @Override
    public boolean hasStatus(Walk walk, WalkStatus... walkStatus) {
        return Arrays.stream(walkStatus).anyMatch(status -> status.equals(walk.getStatus()));
    }

    public WalkResponse.WalkSearchListResponse buildWalkSearchListResponse(Member member, Slice<Walk> walkSlice) {
        List<Walk> walks = walkSlice.getContent();
        boolean hasNext = walkSlice.hasNext();
        Long cursor = null;
        if (hasNext && !walks.isEmpty()) {
            cursor = walks.get(walks.size() - 1).getId();
        }

        List<WalkResponse.WalkSearchResponse> responses = new ArrayList<>();
        walks.forEach(walk ->
            responses.add(WalkConverter.toWalkSearchResponse(
                    walk,
                    memberWalkRepository.findTopByWalkOrderByCreatedAtAsc(walk).getMember().getNickname(),
                    // FIXME: 쿼리 수정 필요
                    walkLikeRepository.existsByMemberAndWalk(member, walk)
            ))
        );

        return WalkConverter.toWalkSearchListResponse(responses, cursor, hasNext);
    }
}
