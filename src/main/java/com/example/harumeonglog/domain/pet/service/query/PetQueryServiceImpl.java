package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.infrastructure.MemberRepository;
import com.example.harumeonglog.domain.pet.converter.MemberPetConverter;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import com.example.harumeonglog.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PetQueryServiceImpl implements PetQueryService {
    private final MemberPetRepository memberPetRepository;
    private final MemberRepository memberRepository;
    private final S3Util s3Util;

    @Override
    public PetResponse.GetPetsResponse getPets(Long cursor, int size, Member member) {

        Pageable pageable = PageRequest.of(0, size);

        Slice<MemberPet> memberPetSlice;
        if (cursor == null || cursor == 0L) {
            memberPetSlice = memberPetRepository.findFirstPageByMemberId(member.getId(), pageable);
        } else {
            memberPetSlice = memberPetRepository.findByMemberAndCursor(member.getId(), cursor, pageable);
        }

        List<MemberPet> currentMemberPets = memberPetSlice.getContent();
        List<Long> petIds = currentMemberPets.stream()
                .map(mp -> mp.getPet().getId())
                .collect(Collectors.toList());

        List<MemberPet> relatedMemberPets = petIds.isEmpty() ?
                Collections.emptyList() :
                memberPetRepository.findByPetIdsAndNotMemberId(petIds, member.getId());

        return MemberPetConverter.toGetPetsResponse(memberPetSlice, currentMemberPets, relatedMemberPets, s3Util);
    }

    @Override
    public PetResponse.PetListPreviewResponse getChangePet(Long cursor, int size, Member member) {

        Pageable pageable = PageRequest.of(0, size);

        Slice<MemberPet> memberPetSlice;
        if (cursor == null || cursor == 0L) {
            memberPetSlice = memberPetRepository.findFirstPageByMemberId(member.getId(), pageable);
        } else {
            memberPetSlice = memberPetRepository.findByMemberAndCursor(member.getId(), cursor, pageable);
        }

        return MemberPetConverter.toGetChangePetResponse(memberPetSlice, s3Util);
    }

    @Override
    public PetResponse.SearchMemberResponse searchMember(String email, Member member, Long cursor, int size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<Member> memberSlice;

        if (cursor == null || cursor == 0L) {
            memberSlice = memberRepository.findByEmailContaining(email, member.getId(), pageable);
        } else {
            memberSlice = memberRepository.findByEmailContainingAndCursor(email, member.getId(), cursor, pageable);
        }

        return MemberPetConverter.toSearchMemberResponse(memberSlice, s3Util);
    }
}
