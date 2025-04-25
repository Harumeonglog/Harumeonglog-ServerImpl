package com.example.harumeonglog.domain.walk.converter;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.entity.Pet;
import com.example.harumeonglog.domain.walk.dto.response.WalkResponse;
import com.example.harumeonglog.domain.walk.entity.Walk;

import java.util.List;

public class WalkConverter {

    public static WalkResponse.WalkStartResponse toWalkStartResponse(Walk walk) {
        return WalkResponse.WalkStartResponse.builder()
                .walkId(walk.getId())
                .createdAt(walk.getCreatedAt())
                .updatedAt(walk.getUpdatedAt())
                .build();
    }

    public static WalkResponse.WalkAvailablePetListResponse toWalkAvailablePetListResponse(List<Pet> pets) {
        return WalkResponse.WalkAvailablePetListResponse.builder()
                .pets(pets.stream().map(WalkConverter::toWalkAvailablePetInfoResponse).toList())
                .size(pets.size())
                .build();
    }

    public static WalkResponse.WalkAvailablePetInfoResponse toWalkAvailablePetInfoResponse(Pet pet) {
        return WalkResponse.WalkAvailablePetInfoResponse.builder()
                .petId(pet.getId())
                .name(pet.getName())
                .image(pet.getMainImage())
                .build();
    }

    public static WalkResponse.WalkAvailableMemberListResponse toWalkAvailableMemberListResponse(List<Member> members) {
        return WalkResponse.WalkAvailableMemberListResponse.builder()
                .members(members.stream().map(WalkConverter::toWalkAvailableMemberResponse).toList())
                .size(members.size())
                .build();
    }

    public static WalkResponse.WalkAvailableMemberResponse toWalkAvailableMemberResponse(Member member) {
            return WalkResponse.WalkAvailableMemberResponse.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .build();
    }

    public static WalkResponse.WalkSearchListResponse toWalkSearchListResponse(List<WalkResponse.WalkSearchResponse> items, Long cursor, boolean hasNext) {
        return WalkResponse.WalkSearchListResponse.builder()
                .items(items)
                .hasNext(hasNext)
                .cursor(cursor)
                .build();
    }
}
