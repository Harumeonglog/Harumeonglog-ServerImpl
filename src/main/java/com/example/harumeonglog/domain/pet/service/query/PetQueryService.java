package com.example.harumeonglog.domain.pet.service.query;

import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.pet.dto.response.PetResponse;

public interface PetQueryService {
    PetResponse.GetPetsResponse getPets(Long cursor, int size, Member member);
    PetResponse.SearchMemberResponse searchMember(String email, Long cursor, int size);
}
