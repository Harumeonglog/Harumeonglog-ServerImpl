package com.example.harumeonglog.domain.pet.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.MemberPetRole;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;
import com.example.harumeonglog.domain.pet.entity.MemberPet;
import com.example.harumeonglog.domain.pet.entity.Pet;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberPetConverter {
    public static MemberPet toMemberPet( Member member, Pet pet, MemberPetRole role){
        return MemberPet.builder()
                .pet(pet)
                .member(member)
                .role(role)
                .build();
    }

    public static PetResponse.GetPetsResponse toGetPetsResponse(
            Slice<MemberPet> memberPetSlice,
            List<MemberPet> currentMemberPets,
            List<MemberPet> relatedMemberPets) {
        List<PetResponse.PetInfo> petInfos = currentMemberPets.stream()
                .map(mp -> toPetInfoResponse(mp, relatedMemberPets))
                .collect(Collectors.toList());

        Long nextCursor = memberPetSlice.hasNext() ?
                currentMemberPets.get(currentMemberPets.size() - 1).getId() : null;

        return PetResponse.GetPetsResponse.builder()
                .pets(petInfos)
                .cursor(nextCursor)
                .hasNext(memberPetSlice.hasNext())
                .build();
    }

    public static PetResponse.PetInfo toPetInfoResponse(
            MemberPet memberPet,
            List<MemberPet> relatedMemberPets) {
        Pet pet = memberPet.getPet();
        // 현재 MemberPet을 PeopleInfo로 변환
        PetResponse.PeopleInfo currentPeopleInfo = PetResponse.PeopleInfo.builder()
                .id(memberPet.getMember().getId())
                .name(memberPet.getMember().getNickname())
                .role(memberPet.getRole().name())
                .build();

        // 관련 MemberPet을 PeopleInfo로 변환
        List<PetResponse.PeopleInfo> relatedPeopleInfos = relatedMemberPets.stream()
                .filter(mp -> mp.getPet().getId().equals(pet.getId()))
                .map(mp -> PetResponse.PeopleInfo.builder()
                        .id(mp.getMember().getId())
                        .name(mp.getMember().getNickname())
                        .role(mp.getRole().name())
                        .build())
                .toList();

        // PeopleInfo 병합
        List<PetResponse.PeopleInfo> peopleInfos = new ArrayList<>();
        peopleInfos.add(currentPeopleInfo);
        peopleInfos.addAll(relatedPeopleInfos);

        return PetResponse.PetInfo.builder()
                .role(memberPet.getRole().name())
                .petId(pet.getId())
                .name(pet.getName())
                .size(pet.getSize())
                .type(pet.getType())
                .gender(pet.getGender())
                .birth(pet.getBirth())
                .mainImage(pet.getMainImage())
                .people(peopleInfos)
                .build();
    }
}
