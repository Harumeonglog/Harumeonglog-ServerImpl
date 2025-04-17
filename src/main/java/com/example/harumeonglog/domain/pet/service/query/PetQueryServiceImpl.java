package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.converter.MemberPetConverter;
import com.example.harumeonglog.domain.pet.converter.PetConverter;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.pet.repository.MemberPetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetQueryServiceImpl implements PetQueryService {
    private final MemberPetRepository memberPetRepository;

    @Override
    public PetResponse.GetPetsResponse getPets(Long cursor, int size, Member member) {

        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        Slice<MemberPet> memberPetSlice;
        if (cursor == null || cursor == 0L) {
            memberPetSlice = memberPetRepository.findFirstPageByMemberId(member.getId(), pageable);
        } else {
            memberPetSlice = memberPetRepository.findByMemberAndCursor(member.getId(), cursor, pageable);
        }

        // 현재 로그인한 Member의 MemberPet 조회
        List<MemberPet> currentMemberPets = memberPetSlice.getContent();
        List<Long> petIds = currentMemberPets.stream()
                .map(mp -> mp.getPet().getId())
                .collect(Collectors.toList());

        // 다른 Member의 MemberPet 조회 (로그인한 Member 제외)
        List<MemberPet> relatedMemberPets = petIds.isEmpty() ?
                Collections.emptyList() :
                memberPetRepository.findByPetIdsAndNotMemberId(petIds, member.getId());

        return MemberPetConverter.toGetPetsResponse(memberPetSlice, currentMemberPets, relatedMemberPets);
    }

    @Override
    public PetResponse.SearchMemberResponse searchMember(String email, Long cursor, int size) {
        return null;
    }
}
