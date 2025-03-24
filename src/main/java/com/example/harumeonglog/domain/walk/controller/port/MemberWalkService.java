package com.example.harumeonglog.domain.walk.controller.port;

import com.example.harumeonglog.domain.member.domain.Member;
import com.example.harumeonglog.domain.pet.domain.Pet;
import com.example.harumeonglog.domain.walk.controller.dto.request.MemberWalkRequest;

import java.util.List;

public interface MemberWalkService {
    List<Pet> getPets(Member member);
    List<Member> getMembers(MemberWalkRequest.PetMemberRequest dto);
}
